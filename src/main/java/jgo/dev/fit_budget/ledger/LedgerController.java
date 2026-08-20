package jgo.dev.fit_budget.ledger;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.ledger.dto.LedgerColorRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerRequest;
import jgo.dev.fit_budget.ledger.dto.LedgerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public List<LedgerResponse> getLedgers() {
        return ledgerService.getLedgers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LedgerResponse createLedger(@Valid @RequestBody LedgerRequest request) {
        return ledgerService.createLedger(request);
    }

    @PutMapping("/{id}")
    public LedgerResponse updateLedger(@PathVariable UUID id, @Valid @RequestBody LedgerRequest request) {
        return ledgerService.updateLedger(id, request);
    }

    @PatchMapping("/{id}/color")
    public LedgerResponse updateColor(@PathVariable UUID id, @Valid @RequestBody LedgerColorRequest request) {
        return ledgerService.updateColor(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLedger(@PathVariable UUID id) {
        ledgerService.deleteLedger(id);
    }
}
