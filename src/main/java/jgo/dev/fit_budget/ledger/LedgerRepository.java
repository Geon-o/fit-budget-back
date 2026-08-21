package jgo.dev.fit_budget.ledger;

import jgo.dev.fit_budget.ledger.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerRepository extends JpaRepository<Ledger, UUID> {
    List<Ledger> findByOwnerId(UUID ownerId);
}
