package jgo.dev.fit_budget.auth;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.auth.dto.GoogleLoginRequest;
import jgo.dev.fit_budget.auth.dto.NaverLoginRequest;
import jgo.dev.fit_budget.auth.dto.RefreshRequest;
import jgo.dev.fit_budget.auth.dto.TokenResponse;
import jgo.dev.fit_budget.auth.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal OAuth2User principal) {
        return authService.getCurrentUser(principal);
    }

    @PostMapping("/login/google")
    public TokenResponse loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        return authService.loginWithGoogle(request.idToken());
    }

    @PostMapping("/login/naver")
    public TokenResponse loginWithNaver(@Valid @RequestBody NaverLoginRequest request) {
        return authService.loginWithNaver(request.code(), request.state());
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }
}
