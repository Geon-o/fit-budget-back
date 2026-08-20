package jgo.dev.fit_budget.budget;

import jgo.dev.fit_budget.budget.dto.BudgetRequest;
import jgo.dev.fit_budget.budget.dto.BudgetResponse;
import jgo.dev.fit_budget.budget.entity.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<BudgetResponse> getBudgets(UUID ledgerId) {
        return budgetRepository.findByLedgerId(ledgerId).stream()
                .map(BudgetResponse::from)
                .toList();
    }

    @Transactional
    public BudgetResponse saveBudget(UUID ledgerId, BudgetRequest request) {
        Budget budget = budgetRepository.findByLedgerIdAndYearMonth(ledgerId, request.yearMonth())
                .orElse(null);

        if (budget == null) {
            budget = budgetRepository.save(new Budget(
                    ledgerId, request.yearMonth(), request.monthlyBudget(), request.savingGoal(), request.paymentDate()));
        } else {
            budget.update(request.monthlyBudget(), request.savingGoal(), request.paymentDate());
        }

        return BudgetResponse.from(budget);
    }
}
