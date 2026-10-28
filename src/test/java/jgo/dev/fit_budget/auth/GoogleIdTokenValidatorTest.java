package jgo.dev.fit_budget.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoogleIdTokenValidatorTest {

    private final OAuth2TokenValidator<Jwt> validator = AuthService.googleIdTokenValidator(List.of("our-app"));

    private static Jwt idToken(String iss, String aud) {
        return Jwt.withTokenValue("t").header("alg", "RS256")
                .issuer(iss).audience(List.of(aud))
                .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(60))
                .build();
    }

    @Test
    void acceptsOnlyOurAudienceAndGoogleIssuer() {
        assertFalse(validator.validate(idToken("https://accounts.google.com", "our-app")).hasErrors());
        assertFalse(validator.validate(idToken("accounts.google.com", "our-app")).hasErrors());
        // 다른 앱에 발급된 토큰(토큰 바꿔치기)과 다른 발급자는 거부
        assertTrue(validator.validate(idToken("https://accounts.google.com", "other-app")).hasErrors());
        assertTrue(validator.validate(idToken("https://evil.example", "our-app")).hasErrors());
    }
}
