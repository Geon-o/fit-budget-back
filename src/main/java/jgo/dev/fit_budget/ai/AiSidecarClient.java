package jgo.dev.fit_budget.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AiSidecarClient {

    private final RestClient aiSidecarRestClient;

    public String ask(String question, Map<String, Object> summary) {
        try {
            SidecarResponse response = aiSidecarRestClient.post()
                    .uri("/ai/query")
                    .body(new SidecarRequest(question, summary))
                    .retrieve()
                    .body(SidecarResponse.class);

            if (response == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "AI 응답을 받지 못했어요");
            }
            return response.answer();
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "AI 서비스에 연결할 수 없어요", e);
        } catch (HttpStatusCodeException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "AI 응답을 받지 못했어요", e);
        }
    }

    private record SidecarRequest(String question, Map<String, Object> summary) {
    }

    private record SidecarResponse(String answer) {
    }
}
