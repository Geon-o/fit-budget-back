package jgo.dev.fit_budget.expense.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "expense")
@Getter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String category;

    private String memo;

    @Column(nullable = false)
    private String source;

    public Expense(LocalDate expenseDate, Long amount, String category, String memo) {
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
        this.source = "manual";
    }

    public void update(LocalDate expenseDate, Long amount, String category, String memo) {
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
    }
}
