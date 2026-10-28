package jgo.dev.fit_budget.auth;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtProvider {

    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";

    private static final Duration ACCESS_TTL = Duration.ofHours(1);
    private static final Duration REFRESH_TTL = Duration.ofDays(30);

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public JwtProvider(@Value("${app.jwt.secret}") String secret) {
        // HS256은 32바이트 이상 키가 필요하다. 짧으면 기동은 되고 첫 토큰 발급에서야 터지므로 시작 시점에 막는다.
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET은 32바이트 이상이어야 합니다");
        }
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        this.decoder = NimbusJwtDecoder.withSecretKey(key).build();
    }

    public String issueAccess(UUID userId) {
        return issue(userId, ACCESS, ACCESS_TTL);
    }

    // ponytail: stateless refresh라 서버에서 폐기 불가 — 로그아웃 즉시 무효화가 필요하면 refresh 저장 테이블 추가
    public String issueRefresh(UUID userId) {
        return issue(userId, REFRESH, REFRESH_TTL);
    }

    /** 서명·만료가 유효하고 type이 일치하면 userId, 아니면 empty. (subject가 이메일이던 예전 토큰도 empty) */
    public Optional<UUID> verify(String token, String type) {
        try {
            Jwt jwt = decoder.decode(token);
            return type.equals(jwt.getClaimAsString("type")) ? Optional.of(UUID.fromString(jwt.getSubject())) : Optional.empty();
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private String issue(UUID userId, String type, Duration ttl) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .claim("type", type)
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }
}
