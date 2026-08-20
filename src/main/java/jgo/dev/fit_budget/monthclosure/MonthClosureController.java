package jgo.dev.fit_budget.monthclosure;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledgers/{ledgerId}/month-closures")
@RequiredArgsConstructor
public class MonthClosureController {

    private final MonthClosureService monthClosureService;

    @GetMapping
    public List<String> getClosedMonths(@PathVariable UUID ledgerId) {
        return monthClosureService.getClosedMonths(ledgerId);
    }

    @PostMapping("/{yearMonth}")
    @ResponseStatus(HttpStatus.CREATED)
    public void close(@PathVariable UUID ledgerId, @PathVariable("yearMonth") String yearMonth) {
        monthClosureService.close(ledgerId, yearMonth);
    }

    @DeleteMapping("/{yearMonth}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reopen(@PathVariable UUID ledgerId, @PathVariable("yearMonth") String yearMonth) {
        monthClosureService.reopen(ledgerId, yearMonth);
    }
}
