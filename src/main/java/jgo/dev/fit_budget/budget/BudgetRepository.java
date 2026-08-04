package jgo.dev.fit_budget.budget;

import jgo.dev.fit_budget.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Optional<Budget> findByYearMonth(String yearMonth);
}
