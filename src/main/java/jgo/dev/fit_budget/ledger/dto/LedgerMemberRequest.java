package jgo.dev.fit_budget.ledger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LedgerMemberRequest(
        @NotBlank @Email String email
) {
}
