package jgo.dev.fit_budget.recurringexpense;

import jgo.dev.fit_budget.recurringexpense.dto.RecurringExpenseRequest;
import jgo.dev.fit_budget.recurringexpense.dto.RecurringExpenseResponse;
import jgo.dev.fit_budget.recurringexpense.entity.RecurringExpense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;

    public List<RecurringExpenseResponse> getRecurringExpenses() {
        return recurringExpenseRepository.findAll().stream()
                .map(RecurringExpenseResponse::from)
                .toList();
    }

    public RecurringExpenseResponse addRecurringExpense(RecurringExpenseRequest request) {
        RecurringExpense recurringExpense = new RecurringExpense(
                request.memo(), request.dayOfMonth(), request.amount());
        return RecurringExpenseResponse.from(recurringExpenseRepository.save(recurringExpense));
    }

    public void deleteRecurringExpense(UUID id) {
        recurringExpenseRepository.deleteById(id);
    }
}
