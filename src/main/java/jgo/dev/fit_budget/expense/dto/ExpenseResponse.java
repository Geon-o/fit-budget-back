package jgo.dev.fit_budget.expense.dto;

import jgo.dev.fit_budget.expense.entity.Expense;

import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        LocalDate expenseDate,
        Long amount,
        String category,
        String memo,
        String source
) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getExpenseDate(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getMemo(),
                expense.getSource()
        );
    }
}
