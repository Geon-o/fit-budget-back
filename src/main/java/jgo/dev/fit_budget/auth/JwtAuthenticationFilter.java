package jgo.dev.fit_budget.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// @Component 아님: 빈으로 두면 서블릿 컨테이너에도 따로 등록돼 시큐리티 체인 밖에서 먼저 실행될 수 있다. SecurityConfig에서 생성한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER)) {
            jwtProvider.verify(header.substring(BEARER.length()), JwtProvider.ACCESS).ifPresent(email -> {
                // 컨트롤러·LedgerAccessInterceptor가 OAuth2User의 "email" 속성을 읽으므로 같은 타입으로 맞춘다.
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                var principal = new DefaultOAuth2User(authorities, Map.of("email", email), "email");
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, authorities));
            });
        }
        chain.doFilter(request, response);
    }
}
