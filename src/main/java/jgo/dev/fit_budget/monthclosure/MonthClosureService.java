package jgo.dev.fit_budget.monthclosure;

import jgo.dev.fit_budget.monthclosure.entity.MonthClosure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonthClosureService {

    private final MonthClosureRepository monthClosureRepository;

    public List<String> getClosedMonths(UUID ledgerId) {
        return monthClosureRepository.findByLedgerId(ledgerId).stream()
                .map(MonthClosure::getYearMonth)
                .toList();
    }

    @Transactional
    public void close(UUID ledgerId, String yearMonth) {
        if (!monthClosureRepository.existsByLedgerIdAndYearMonth(ledgerId, yearMonth)) {
            monthClosureRepository.save(new MonthClosure(ledgerId, yearMonth));
        }
    }

    @Transactional
    public void reopen(UUID ledgerId, String yearMonth) {
        monthClosureRepository.deleteByLedgerIdAndYearMonth(ledgerId, yearMonth);
    }
}
