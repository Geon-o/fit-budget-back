package jgo.dev.fit_budget.ledger;

import jgo.dev.fit_budget.auth.UserRepository;
import jgo.dev.fit_budget.auth.entity.User;
import jgo.dev.fit_budget.ledger.entity.Ledger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class LedgerAccessGuard {

    private final LedgerRepository ledgerRepository;
    private final LedgerMemberRepository ledgerMemberRepository;
    private final UserRepository userRepository;

    public void checkAccess(String email, UUID ledgerId) {
        if (email == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"));
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));

        boolean hasAccess = ledger.getOwnerId().equals(user.getId())
                || ledgerMemberRepository.existsByLedgerIdAndUserId(ledgerId, user.getId());
        if (!hasAccess) {
            throw new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다");
        }
    }
}
