package jgo.dev.fit_budget.expense;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.expense.dto.ExpenseRequest;
import jgo.dev.fit_budget.expense.dto.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledgers/{ledgerId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public List<ExpenseResponse> getExpenses(@PathVariable UUID ledgerId) {
        return expenseService.getExpenses(ledgerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse addExpense(@PathVariable UUID ledgerId, @Valid @RequestBody ExpenseRequest request) {
        return expenseService.addExpense(ledgerId, request);
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable UUID ledgerId, @PathVariable("id") UUID id, @Valid @RequestBody ExpenseRequest request) {
        return expenseService.updateExpense(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable UUID ledgerId, @PathVariable("id") UUID id) {
        expenseService.deleteExpense(id);
    }
}
