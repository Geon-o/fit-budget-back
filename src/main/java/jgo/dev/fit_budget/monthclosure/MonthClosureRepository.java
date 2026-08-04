package jgo.dev.fit_budget.monthclosure;

import jgo.dev.fit_budget.monthclosure.entity.MonthClosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonthClosureRepository extends JpaRepository<MonthClosure, UUID> {
    boolean existsByYearMonth(String yearMonth);
    void deleteByYearMonth(String yearMonth);
}
