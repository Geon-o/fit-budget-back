package jgo.dev.fit_budget.recurringexpense.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecurringExpenseRequest(
        @NotBlank String memo,
        @NotNull @Min(1) @Max(31) Integer dayOfMonth,
        @Positive Long amount
) {
}
