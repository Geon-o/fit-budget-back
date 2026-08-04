package jgo.dev.fit_budget.monthclosure;

import jgo.dev.fit_budget.monthclosure.entity.MonthClosure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthClosureService {

    private final MonthClosureRepository monthClosureRepository;

    public List<String> getClosedMonths() {
        return monthClosureRepository.findAll().stream()
                .map(MonthClosure::getYearMonth)
                .toList();
    }

    @Transactional
    public void close(String yearMonth) {
        if (!monthClosureRepository.existsByYearMonth(yearMonth)) {
            monthClosureRepository.save(new MonthClosure(yearMonth));
        }
    }

    @Transactional
    public void reopen(String yearMonth) {
        monthClosureRepository.deleteByYearMonth(yearMonth);
    }
}
