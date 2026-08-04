package jgo.dev.fit_budget.budget.entity;

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
@Table(name = "budget")
@Getter
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String yearMonth;

    @Column(nullable = false)
    private Long monthlyBudget;

    @Column(nullable = false)
    private Long savingGoal;

    @Column(nullable = false)
    private LocalDate paymentDate;

    public Budget(String yearMonth, Long monthlyBudget, Long savingGoal, LocalDate paymentDate) {
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
