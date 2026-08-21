package jgo.dev.fit_budget.auth;

import jgo.dev.fit_budget.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;
        if ("naver".equals(registrationId)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        } else {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }

        userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email, name, NicknameGenerator.generate())));

        // 프로바이더마다 속성 키가 달라서(google: email 최상위, naver: response 안에 중첩),
        // 이후 코드는 이 정규화된 email 속성 하나만 보면 되도록 통일한다.
        Map<String, Object> normalizedAttributes = new HashMap<>(attributes);
        normalizedAttributes.put("email", email);
        normalizedAttributes.put("name", name);

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), normalizedAttributes, "email");
    }
}
