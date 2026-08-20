package jgo.dev.fit_budget.monthclosure;

import jgo.dev.fit_budget.monthclosure.entity.MonthClosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MonthClosureRepository extends JpaRepository<MonthClosure, UUID> {
    List<MonthClosure> findByLedgerId(UUID ledgerId);
    boolean existsByLedgerIdAndYearMonth(UUID ledgerId, String yearMonth);
    void deleteByLedgerIdAndYearMonth(UUID ledgerId, String yearMonth);
    void deleteByLedgerId(UUID ledgerId);
}
