package jgo.dev.fit_budget.ledger;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.ledger.dto.LedgerColorRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerMemberRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerMemberResponse;
import jgo.dev.fit_budget.ledger.dto.LedgerRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledgers")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping
    public List<LedgerResponse> getLedgers(@AuthenticationPrincipal OAuth2User principal) {
        return ledgerService.getLedgers(userId(principal));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LedgerResponse createLedger(
            @AuthenticationPrincipal OAuth2User principal,
            @Valid @RequestBody LedgerRequest request
    ) {
        return ledgerService.createLedger(userId(principal), request);
    }

    @PutMapping("/{id}")
    public LedgerResponse updateLedger(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable("id") UUID id,
            @Valid @RequestBody LedgerRequest request
    ) {
        return ledgerService.updateLedger(userId(principal), id, request);
    }

    @PatchMapping("/{id}/color")
    public LedgerResponse updateColor(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable("id") UUID id,
            @Valid @RequestBody LedgerColorRequest request
    ) {
        return ledgerService.updateColor(userId(principal), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLedger(@AuthenticationPrincipal OAuth2User principal, @PathVariable("id") UUID id) {
        ledgerService.deleteLedger(userId(principal), id);
    }

    @GetMapping("/{id}/members")
    public List<LedgerMemberResponse> getMembers(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable("id") UUID id
    ) {
        return ledgerService.getMembers(userId(principal), id);
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public LedgerMemberResponse inviteMember(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable("id") UUID id,
            @Valid @RequestBody LedgerMemberRequest request
    ) {
        return ledgerService.inviteMember(userId(principal), id, request);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable("id") UUID id,
            @PathVariable("memberId") UUID memberId
    ) {
        ledgerService.removeMember(userId(principal), id, memberId);
    }

    private String userId(OAuth2User principal) {
        return principal == null ? null : principal.getAttribute("userId");
    }
}
