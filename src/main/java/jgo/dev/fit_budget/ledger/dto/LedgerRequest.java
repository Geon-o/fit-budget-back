package jgo.dev.fit_budget.ledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LedgerRequest(
        @NotBlank @Size(max = 10) String name
) {
}
