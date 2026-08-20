package jgo.dev.fit_budget.budget;

import jgo.dev.fit_budget.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByLedgerId(UUID ledgerId);
    Optional<Budget> findByLedgerIdAndYearMonth(UUID ledgerId, String yearMonth);
    void deleteByLedgerId(UUID ledgerId);
}
