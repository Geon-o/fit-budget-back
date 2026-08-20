package jgo.dev.fit_budget.budget.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "budget", uniqueConstraints = @UniqueConstraint(columnNames = {"ledger_id", "year_month"}))
@Getter
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ledgerId;

    @Column(nullable = false)
    private String yearMonth;

    @Column(nullable = false)
    private Long monthlyBudget;

    @Column(nullable = false)
    private Long savingGoal;

    @Column(nullable = false)
    private LocalDate paymentDate;

    public Budget(UUID ledgerId, String yearMonth, Long monthlyBudget, Long savingGoal, LocalDate paymentDate) {
        this.ledgerId = ledgerId;
        this.yearMonth = yearMonth;
        this.monthlyBudget = monthlyBudget;
        this.savingGoal = savingGoal;
        this.paymentDate = paymentDate;
    }

    public void update(Long monthlyBudget, Long savingGoal, LocalDate paymentDate) {
        this.monthlyBudget = monthlyBudget;
        this.savingGoal = savingGoal;
        this.paymentDate = paymentDate;
    }
}
