package jgo.dev.fit_budget.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull LocalDate expenseDate,
        @NotNull @Positive Long amount,
        @NotBlank String category,
        String memo
) {
}
