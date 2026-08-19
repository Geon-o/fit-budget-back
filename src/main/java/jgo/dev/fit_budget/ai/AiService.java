package jgo.dev.fit_budget.ai;

import jgo.dev.fit_budget.budget.BudgetService;
import jgo.dev.fit_budget.budget.dto.BudgetResponse;
import jgo.dev.fit_budget.expense.ExpenseService;
import jgo.dev.fit_budget.expense.dto.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final AiSidecarClient aiSidecarClient;

    public String query(String question) {
        return aiSidecarClient.ask(question, buildCurrentMonthSummary());
    }

    private Map<String, Object> buildCurrentMonthSummary() {
        String yearMonth = YearMonth.now().toString();
        String previousYearMonth = YearMonth.now().minusMonths(1).toString();

        List<ExpenseResponse> expenses = expenseService.getExpenses();

        Map<String, Long> currentMonthExpenseByCategory = expenses.stream()
                .filter(e -> e.expenseDate().toString().startsWith(yearMonth))
                .collect(Collectors.groupingBy(ExpenseResponse::category, Collectors.summingLong(ExpenseResponse::amount)));

        long currentMonthTotalExpense = currentMonthExpenseByCategory.values().stream().mapToLong(Long::longValue).sum();

        long previousMonthTotalExpense = expenses.stream()
                .filter(e -> e.expenseDate().toString().startsWith(previousYearMonth))
                .mapToLong(ExpenseResponse::amount)
                .sum();

        BudgetResponse budget = budgetService.getBudgets().stream()
                .filter(b -> b.yearMonth().equals(yearMonth))
                .findFirst()
                .orElse(null);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("yearMonth", yearMonth);
        summary.put("monthlyBudget", budget != null ? budget.monthlyBudget() : null);
        summary.put("savingGoal", budget != null ? budget.savingGoal() : null);
        summary.put("currentMonthTotalExpense", currentMonthTotalExpense);
        summary.put("currentMonthExpenseByCategory", currentMonthExpenseByCategory);
        summary.put("previousYearMonth", previousYearMonth);
        summary.put("previousMonthTotalExpense", previousMonthTotalExpense);
        return summary;
    }
}
