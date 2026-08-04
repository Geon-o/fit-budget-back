package jgo.dev.fit_budget.monthclosure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "month_closure")
@Getter
@NoArgsConstructor
public class MonthClosure {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String yearMonth;

    public MonthClosure(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
