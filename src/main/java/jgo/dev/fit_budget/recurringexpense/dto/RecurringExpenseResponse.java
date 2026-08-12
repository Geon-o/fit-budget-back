package jgo.dev.fit_budget.recurringexpense.dto;

import jgo.dev.fit_budget.recurringexpense.entity.RecurringExpense;

import java.util.UUID;

public record RecurringExpenseResponse(
        UUID id,
        String memo,
        Integer dayOfMonth,
        Long amount
) {
    public static RecurringExpenseResponse from(RecurringExpense recurringExpense) {
        return new RecurringExpenseResponse(
                recurringExpense.getId(),
                recurringExpense.getMemo(),
                recurringExpense.getDayOfMonth(),
                recurringExpense.getAmount()
        );
    }
}
