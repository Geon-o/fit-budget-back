package jgo.dev.fit_budget.ai;

import jakarta.validation.Valid;
import jgo.dev.fit_budget.ai.dto.AiQueryRequest;
import jgo.dev.fit_budget.ai.dto.AiQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/query")
    public AiQueryResponse query(@Valid @RequestBody AiQueryRequest request) {
        return new AiQueryResponse(aiService.query(request.question()));
    }
}
