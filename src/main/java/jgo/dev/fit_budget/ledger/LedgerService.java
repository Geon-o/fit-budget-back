package jgo.dev.fit_budget.ledger;

import jgo.dev.fit_budget.budget.BudgetRepository;
import jgo.dev.fit_budget.expense.ExpenseRepository;
import jgo.dev.fit_budget.ledger.dto.LedgerColorRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerResponse;
import jgo.dev.fit_budget.ledger.entity.Ledger;
import jgo.dev.fit_budget.monthclosure.MonthClosureRepository;
import jgo.dev.fit_budget.recurringexpense.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final MonthClosureRepository monthClosureRepository;

    public List<LedgerResponse> getLedgers() {
        return ledgerRepository.findAll().stream()
                .map(LedgerResponse::from)
                .toList();
    }

    public LedgerResponse createLedger(LedgerRequest request) {
        Ledger ledger = new Ledger(request.name());
        return LedgerResponse.from(ledgerRepository.save(ledger));
    }

    @Transactional
    public LedgerResponse updateLedger(UUID id, LedgerRequest request) {
        Ledger ledger = ledgerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        ledger.update(request.name());
        return LedgerResponse.from(ledger);
    }

    @Transactional
    public LedgerResponse updateColor(UUID id, LedgerColorRequest request) {
        Ledger ledger = ledgerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다"));
        ledger.updateColor(request.color());
        return LedgerResponse.from(ledger);
    }

    @Transactional
    public void deleteLedger(UUID id) {
        if (!ledgerRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "가계부를 찾을 수 없습니다");
        }
        expenseRepository.deleteByLedgerId(id);
        budgetRepository.deleteByLedgerId(id);
        recurringExpenseRepository.deleteByLedgerId(id);
        monthClosureRepository.deleteByLedgerId(id);
        ledgerRepository.deleteById(id);
    }
}
