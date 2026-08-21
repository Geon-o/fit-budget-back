package jgo.dev.fit_budget.ledger;

import jgo.dev.fit_budget.ledger.entity.LedgerMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerMemberRepository extends JpaRepository<LedgerMember, UUID> {
    List<LedgerMember> findByLedgerId(UUID ledgerId);
    List<LedgerMember> findByUserId(UUID userId);
    boolean existsByLedgerIdAndUserId(UUID ledgerId, UUID userId);
    void deleteByLedgerId(UUID ledgerId);
}
