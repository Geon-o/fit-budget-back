package jgo.dev.fit_budget.ledger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.UUID;

// /api/ledgers/{ledgerId}/... 하위 요청(예산/지출/고정지출/월마감/AI)마다 컨트롤러에서 각각
// 소유권 검증을 하는 대신, 여기서 한 번에 "이 유저가 이 ledgerId에 접근 가능한지"를 확인한다.
@Component
@RequiredArgsConstructor
public class LedgerAccessInterceptor implements HandlerInterceptor {

    private final LedgerAccessGuard ledgerAccessGuard;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String ledgerIdValue = pathVariables == null ? null : pathVariables.get("ledgerId");
        if (ledgerIdValue == null) {
            return true;
        }

        UUID ledgerId;
        try {
            ledgerId = UUID.fromString(ledgerIdValue);
        } catch (IllegalArgumentException e) {
            return true;
        }

        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ledgerAccessGuard.checkAccess(principal.getAttribute("userId"), ledgerId);
        return true;
    }
}
