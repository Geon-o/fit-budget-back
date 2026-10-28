package jgo.dev.fit_budget.ledger.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// ponytail: 초대 방식(초대 코드/링크 등)은 초대 화면 이식 때 결정 — 그때까지는 사용자 ID로 초대
public record LedgerMemberRequest(
        @NotNull UUID userId
) {
}
