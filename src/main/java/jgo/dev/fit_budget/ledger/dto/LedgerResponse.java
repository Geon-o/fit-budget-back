package jgo.dev.fit_budget.ledger.dto;

import jgo.dev.fit_budget.ledger.entity.Ledger;

import java.util.UUID;

public record LedgerResponse(
        UUID id,
        String name,
        String color
) {
    public static LedgerResponse from(Ledger ledger) {
        return new LedgerResponse(ledger.getId(), ledger.getName(), ledger.getColor());
    }
}
