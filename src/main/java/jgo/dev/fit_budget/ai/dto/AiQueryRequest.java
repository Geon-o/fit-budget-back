package jgo.dev.fit_budget.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record AiQueryRequest(
        @NotBlank String question
) {
}
