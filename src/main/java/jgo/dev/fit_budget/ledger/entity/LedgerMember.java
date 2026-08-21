package jgo.dev.fit_budget.ledger.entity;

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
@Table(name = "ledger_member", uniqueConstraints = @UniqueConstraint(columnNames = {"ledger_id", "user_id"}))
@Getter
@NoArgsConstructor
public class LedgerMember {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ledgerId;

    @Column(nullable = false)
    private UUID userId;

    public LedgerMember(UUID ledgerId, UUID userId) {
        this.ledgerId = ledgerId;
        this.userId = userId;
    }
}
