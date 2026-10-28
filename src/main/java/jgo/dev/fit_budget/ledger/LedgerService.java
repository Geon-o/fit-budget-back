package jgo.dev.fit_budget.ledger;

import jgo.dev.fit_budget.auth.UserRepository;
import jgo.dev.fit_budget.auth.entity.User;
import jgo.dev.fit_budget.budget.BudgetRepository;
import jgo.dev.fit_budget.expense.ExpenseRepository;
import jgo.dev.fit_budget.ledger.dto.LedgerColorRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerMemberRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerMemberResponse;
import jgo.dev.fit_budget.ledger.dto.LedgerRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerResponse;
import jgo.dev.fit_budget.ledger.entity.Ledger;
import jgo.dev.fit_budget.ledger.entity.LedgerMember;
import jgo.dev.fit_budget.monthclosure.MonthClosureRepository;
import jgo.dev.fit_budget.recurringexpense.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final LedgerMemberRepository ledgerMemberRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final MonthClosureRepository monthClosureRepository;

    private User resolveUser(String userId) {
        if (userId == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "로그인이 필요합니다");
        }
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"));
    }

    private boolean hasAccess(UUID ledgerId, Ledger ledger, UUID userId) {
        return ledger.getOwnerId().equals(userId) || ledgerMemberRepository.existsByLedgerIdAndUserId(ledgerId, userId);
    }

    private Ledger findLedgerWithAccess(UUID ledgerId, UUID userId) {
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        if (!hasAccess(ledgerId, ledger, userId)) {
            throw new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다");
        }
        return ledger;
    }

    public List<LedgerResponse> getLedgers(String userId) {
        User user = resolveUser(userId);

        Map<UUID, Ledger> ledgers = new LinkedHashMap<>();
        for (Ledger ledger : ledgerRepository.findByOwnerId(user.getId())) {
            ledgers.put(ledger.getId(), ledger);
        }
        List<UUID> memberLedgerIds = ledgerMemberRepository.findByUserId(user.getId()).stream()
                .map(LedgerMember::getLedgerId)
                .toList();
        for (Ledger ledger : ledgerRepository.findAllById(memberLedgerIds)) {
            ledgers.put(ledger.getId(), ledger);
        }

        return ledgers.values().stream()
                .map(LedgerResponse::from)
                .toList();
    }

    public LedgerResponse createLedger(String userId, LedgerRequest request) {
        User user = resolveUser(userId);
        Ledger ledger = new Ledger(user.getId(), request.name());
        return LedgerResponse.from(ledgerRepository.save(ledger));
    }

    @Transactional
    public LedgerResponse updateLedger(String userId, UUID id, LedgerRequest request) {
        User user = resolveUser(userId);
        Ledger ledger = findLedgerWithAccess(id, user.getId());
        ledger.update(request.name());
        return LedgerResponse.from(ledger);
    }

    @Transactional
    public LedgerResponse updateColor(String userId, UUID id, LedgerColorRequest request) {
        User user = resolveUser(userId);
        Ledger ledger = findLedgerWithAccess(id, user.getId());
        ledger.updateColor(request.color());
        return LedgerResponse.from(ledger);
    }

    @Transactional
    public void deleteLedger(String userId, UUID id) {
        User user = resolveUser(userId);
        Ledger ledger = ledgerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        if (!ledger.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "가계부 소유자만 삭제할 수 있어요");
        }

        expenseRepository.deleteByLedgerId(id);
        budgetRepository.deleteByLedgerId(id);
        recurringExpenseRepository.deleteByLedgerId(id);
        monthClosureRepository.deleteByLedgerId(id);
        ledgerMemberRepository.deleteByLedgerId(id);
        ledgerRepository.deleteById(id);
    }

    public List<LedgerMemberResponse> getMembers(String userId, UUID ledgerId) {
        User user = resolveUser(userId);
        findLedgerWithAccess(ledgerId, user.getId());

        return ledgerMemberRepository.findByLedgerId(ledgerId).stream()
                .map(member -> LedgerMemberResponse.from(member, userRepository.findById(member.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"))))
                .toList();
    }

    @Transactional
    public LedgerMemberResponse inviteMember(String userId, UUID ledgerId, LedgerMemberRequest request) {
        User currentUser = resolveUser(userId);
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        if (!ledger.getOwnerId().equals(currentUser.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "가계부 소유자만 초대할 수 있어요");
        }

        User invitee = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "사용자를 찾을 수 없습니다"));
        if (ledgerMemberRepository.existsByLedgerIdAndUserId(ledgerId, invitee.getId())) {
            throw new ResponseStatusException(CONFLICT, "이미 초대된 사용자예요");
        }
        LedgerMember member = ledgerMemberRepository.save(new LedgerMember(ledgerId, invitee.getId()));
        return LedgerMemberResponse.from(member, invitee);
    }

    @Transactional
    public void removeMember(String userId, UUID ledgerId, UUID memberId) {
        User currentUser = resolveUser(userId);
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        if (!ledger.getOwnerId().equals(currentUser.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "가계부 소유자만 멤버를 내보낼 수 있어요");
        }

        LedgerMember member = ledgerMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "멤버를 찾을 수 없습니다"));
        if (!member.getLedgerId().equals(ledgerId)) {
            throw new ResponseStatusException(NOT_FOUND, "멤버를 찾을 수 없습니다");
        }
        ledgerMemberRepository.deleteById(memberId);
    }
}
