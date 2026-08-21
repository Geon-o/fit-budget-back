package jgo.dev.fit_budget.ledger.dto;

import jgo.dev.fit_budget.auth.entity.User;
import jgo.dev.fit_budget.ledger.entity.LedgerMember;

import java.util.UUID;

public record LedgerMemberResponse(
        UUID id,
        UUID userId,
        String email,
        String name
) {
    public static LedgerMemberResponse from(LedgerMember member, User user) {
        return new LedgerMemberResponse(member.getId(), user.getId(), user.getEmail(), user.getName());
    }
}
