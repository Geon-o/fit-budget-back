package jgo.dev.fit_budget.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record NaverLoginRequest(@NotBlank String code, @NotBlank String state) {
}
