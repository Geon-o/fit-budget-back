package jgo.dev.fit_budget.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jgo.dev.fit_budget.auth.dto.GoogleLoginRequest;
import jgo.dev.fit_budget.auth.dto.NaverLoginRequest;
import jgo.dev.fit_budget.auth.dto.RefreshRequest;
import jgo.dev.fit_budget.auth.dto.TokenResponse;
import jgo.dev.fit_budget.auth.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

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

    /**
     * 네이버는 Callback URL로 http(s)만 받아서 앱 스킴으로 바로 돌아갈 수 없다.
     * 브라우저가 받은 code/state(또는 error)를 그대로 앱 스킴으로 넘겨주기만 한다.
     */
    @GetMapping("/login/naver/callback")
    public ResponseEntity<Void> naverCallback(HttpServletRequest request) {
        URI app = URI.create("fitbudget://naver-login?" + Objects.toString(request.getQueryString(), ""));
        return ResponseEntity.status(HttpStatus.FOUND).location(app).build();
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }
}
