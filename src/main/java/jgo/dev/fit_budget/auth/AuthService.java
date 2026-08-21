package jgo.dev.fit_budget.auth;

import jgo.dev.fit_budget.auth.dto.UserResponse;
import jgo.dev.fit_budget.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserResponse getCurrentUser(OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다");
        }
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"));
        return UserResponse.from(user);
    }
}
