package jgo.dev.fit_budget.budget;

import jgo.dev.fit_budget.budget.dto.BudgetRequest;
import jgo.dev.fit_budget.budget.dto.BudgetResponse;
import jgo.dev.fit_budget.budget.entity.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<BudgetResponse> getBudgets() {
        return budgetRepository.findAll().stream()
                .map(BudgetResponse::from)
                .toList();
    }

    @Transactional
    public BudgetResponse saveBudget(BudgetRequest request) {
        Budget budget = budgetRepository.findByYearMonth(request.yearMonth())
                .orElse(null);

        if (budget == null) {
            budget = budgetRepository.save(new Budget(
                    request.yearMonth(), request.monthlyBudget(), request.savingGoal(), request.paymentDate()));
        } else {
            budget.update(request.monthlyBudget(), request.savingGoal(), request.paymentDate());
        }

        return BudgetResponse.from(budget);
    }
}
