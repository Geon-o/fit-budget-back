package jgo.dev.fit_budget.budget.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record BudgetRequest(
        @NotNull @Pattern(regexp = "\\d{4}-\\d{2}") String yearMonth,
        @NotNull @Positive Long monthlyBudget,
        @NotNull @PositiveOrZero Long savingGoal,
        @NotNull LocalDate paymentDate
) {
}
