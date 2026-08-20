package jgo.dev.fit_budget.expense;

import jgo.dev.fit_budget.expense.entity.Expense;
import jgo.dev.fit_budget.expense.dto.ExpenseRequest;
import jgo.dev.fit_budget.expense.dto.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public List<ExpenseResponse> getExpenses(UUID ledgerId) {
        return expenseRepository.findByLedgerId(ledgerId).stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    public ExpenseResponse addExpense(UUID ledgerId, ExpenseRequest request) {
        Expense expense = new Expense(ledgerId, request.expenseDate(), request.amount(), request.category(), request.memo());
        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    @Transactional
    public ExpenseResponse updateExpense(UUID id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "지출 내역을 찾을 수 없습니다"));
        expense.update(request.expenseDate(), request.amount(), request.category(), request.memo());
        return ExpenseResponse.from(expense);
    }

    public void deleteExpense(UUID id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "지출 내역을 찾을 수 없습니다");
        }
        expenseRepository.deleteById(id);
    }
}
