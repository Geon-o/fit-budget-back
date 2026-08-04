package jgo.dev.fit_budget.budget.dto;

import jgo.dev.fit_budget.budget.entity.Budget;

import java.time.LocalDate;
import java.util.UUID;

public record BudgetResponse(
        UUID id,
        String yearMonth,
        Long monthlyBudget,
        Long savingGoal,
        LocalDate paymentDate
) {
    public static BudgetResponse from(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getYearMonth(),
                budget.getMonthlyBudget(),
                budget.getSavingGoal(),
                budget.getPaymentDate()
        );
    }
}
