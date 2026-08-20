package jgo.dev.fit_budget.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiQueryRequest(
        @NotBlank @Size(max = 500) String question
) {
}
