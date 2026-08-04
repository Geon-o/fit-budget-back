package jgo.dev.fit_budget.budget;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.budget.dto.BudgetRequest;
import jgo.dev.fit_budget.budget.dto.BudgetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public List<BudgetResponse> getBudgets() {
        return budgetService.getBudgets();
    }

    @PostMapping
    public BudgetResponse saveBudget(@Valid @RequestBody BudgetRequest request) {
        return budgetService.saveBudget(request);
    }
}
