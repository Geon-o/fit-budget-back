package jgo.dev.fit_budget.ledger.dto;

import jakarta.validation.constraints.NotBlank;

public record LedgerColorRequest(
        @NotBlank String color
) {
}
