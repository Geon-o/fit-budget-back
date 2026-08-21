package jgo.dev.fit_budget.ai;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.ai.dto.AiQueryRequest;
import jgo.dev.fit_budget.ai.dto.AiQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/ledgers/{ledgerId}/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/query")
    public AiQueryResponse query(@PathVariable("ledgerId") UUID ledgerId, @Valid @RequestBody AiQueryRequest request) {
        return new AiQueryResponse(aiService.query(ledgerId, request.question()));
    }
}
