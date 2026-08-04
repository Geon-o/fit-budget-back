package jgo.dev.fit_budget.expense;

import jgo.dev.fit_budget.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
}
