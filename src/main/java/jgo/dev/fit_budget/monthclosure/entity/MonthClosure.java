package jgo.dev.fit_budget.monthclosure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "month_closure", uniqueConstraints = @UniqueConstraint(columnNames = {"ledger_id", "year_month"}))
@Getter
@NoArgsConstructor
public class MonthClosure {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ledgerId;

    @Column(nullable = false)
    private String yearMonth;

    public MonthClosure(UUID ledgerId, String yearMonth) {
        this.ledgerId = ledgerId;
        this.yearMonth = yearMonth;
    }
}
