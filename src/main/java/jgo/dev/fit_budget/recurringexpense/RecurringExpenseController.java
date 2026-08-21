package jgo.dev.fit_budget.recurringexpense;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.recurringexpense.dto.RecurringExpenseRequest;
import jgo.dev.fit_budget.recurringexpense.dto.RecurringExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledgers/{ledgerId}/recurring-expenses")
@RequiredArgsConstructor
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    @GetMapping
    public List<RecurringExpenseResponse> getRecurringExpenses(@PathVariable("ledgerId") UUID ledgerId) {
        return recurringExpenseService.getRecurringExpenses(ledgerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecurringExpenseResponse addRecurringExpense(
            @PathVariable("ledgerId") UUID ledgerId, @Valid @RequestBody RecurringExpenseRequest request) {
        return recurringExpenseService.addRecurringExpense(ledgerId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecurringExpense(@PathVariable("ledgerId") UUID ledgerId, @PathVariable("id") UUID id) {
        recurringExpenseService.deleteRecurringExpense(id);
    }
}
