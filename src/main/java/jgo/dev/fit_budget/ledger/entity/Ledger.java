package jgo.dev.fit_budget.ledger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ledger")
@Getter
@NoArgsConstructor
public class Ledger {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    private String color;

    public Ledger(UUID ownerId, String name) {
        this.ownerId = ownerId;
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }
}
