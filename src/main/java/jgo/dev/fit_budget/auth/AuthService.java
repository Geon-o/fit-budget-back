package jgo.dev.fit_budget.auth;

import jgo.dev.fit_budget.auth.dto.TokenResponse;
import jgo.dev.fit_budget.auth.dto.UserResponse;
import jgo.dev.fit_budget.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP = new ParameterizedTypeReference<>() {
    };
    private static final Set<String> GOOGLE_ISSUERS = Set.of("accounts.google.com", "https://accounts.google.com");

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final JwtDecoder googleIdTokenDecoder;
    private final String naverClientId;
    private final String naverClientSecret;
    private final RestClient restClient = RestClient.create();

    public AuthService(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            @Value("${app.google.client-ids}") List<String> googleClientIds,
            @Value("${app.naver.client-id}") String naverClientId,
            @Value("${app.naver.client-secret}") String naverClientSecret
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
        decoder.setJwtValidator(googleIdTokenValidator(googleClientIds));
        this.googleIdTokenDecoder = decoder;
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
    }

    public UserResponse getCurrentUser(OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다");
        }
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"));
        return UserResponse.from(user);
    }

    /**
     * 구글 id_token의 서명·iss·aud(우리 앱 client ID)·만료를 검증한다.
     * access token은 다른 앱에 발급된 것도 userinfo가 받아주므로 쓰지 않는다.
     */
    public TokenResponse loginWithGoogle(String idToken) {
        Jwt jwt;
        try {
            jwt = googleIdTokenDecoder.decode(idToken);
        } catch (JwtException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "소셜 로그인 인증에 실패했습니다");
        }
        if (!Boolean.TRUE.equals(jwt.getClaimAsBoolean("email_verified"))) {
            throw new ResponseStatusException(UNAUTHORIZED, "인증된 이메일이 아닙니다");
        }
        return loginAs(jwt.getClaimAsString("email"), jwt.getClaimAsString("name"));
    }

    /**
     * 네이버는 토큰의 발급 대상 앱을 확인할 API가 없어서, 앱이 받은 authorization code를
     * 서버가 우리 client secret으로 교환한다. 교환이 되면 그 토큰은 우리 앱에 발급된 것이다.
     */
    public TokenResponse loginWithNaver(String code, String state) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", naverClientId);
        form.add("client_secret", naverClientSecret);
        form.add("code", code);
        form.add("state", state);
        Map<String, Object> token = call(() -> restClient.post().uri("https://nid.naver.com/oauth2.0/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(MAP));
        // 네이버는 교환 실패도 200 + {"error": ...}로 응답한다.
        String accessToken = token == null ? null : (String) token.get("access_token");
        if (accessToken == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "소셜 로그인 인증에 실패했습니다");
        }

        Map<String, Object> body = call(() -> restClient.get().uri("https://openapi.naver.com/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken)).retrieve().body(MAP));
        @SuppressWarnings("unchecked")
        Map<String, Object> response = body == null ? null : (Map<String, Object>) body.get("response");
        return loginAs(response == null ? null : (String) response.get("email"),
                response == null ? null : (String) response.get("name"));
    }

    public TokenResponse refresh(String refreshToken) {
        String email = jwtProvider.verify(refreshToken, JwtProvider.REFRESH)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "다시 로그인해 주세요"));
        return tokens(email);
    }

    static OAuth2TokenValidator<Jwt> googleIdTokenValidator(List<String> clientIds) {
        return JwtValidators.createDefaultWithValidators(
                new JwtClaimValidator<Object>("iss", iss -> GOOGLE_ISSUERS.contains(String.valueOf(iss))),
                new JwtClaimValidator<Collection<String>>("aud",
                        aud -> aud != null && aud.stream().anyMatch(clientIds::contains)));
    }

    private TokenResponse loginAs(String email, String name) {
        if (email == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "이메일을 가져올 수 없습니다");
        }
        userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email, name, NicknameGenerator.generate())));
        return tokens(email);
    }

    private TokenResponse tokens(String email) {
        return new TokenResponse(jwtProvider.issueAccess(email), jwtProvider.issueRefresh(email));
    }

    private static Map<String, Object> call(Supplier<Map<String, Object>> request) {
        try {
            return request.get();
        } catch (RestClientException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "소셜 로그인 인증에 실패했습니다");
        }
    }
}
