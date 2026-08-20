package jgo.dev.fit_budget.recurringexpense;

import jgo.dev.fit_budget.recurringexpense.entity.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, UUID> {
    List<RecurringExpense> findByLedgerId(UUID ledgerId);
    void deleteByLedgerId(UUID ledgerId);
}
