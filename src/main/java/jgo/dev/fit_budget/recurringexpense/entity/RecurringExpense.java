package jgo.dev.fit_budget.recurringexpense.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "recurring_expense")
@Getter
@NoArgsConstructor
public class RecurringExpense {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private Integer dayOfMonth;

    private Long amount;

    public RecurringExpense(String memo, Integer dayOfMonth, Long amount) {
        this.memo = memo;
        this.dayOfMonth = dayOfMonth;
        this.amount = amount;
    }
}
