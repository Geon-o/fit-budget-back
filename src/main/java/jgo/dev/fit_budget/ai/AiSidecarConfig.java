package jgo.dev.fit_budget.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AiSidecarConfig {

    @Bean
    public RestClient aiSidecarRestClient(
            @Value("${ai.service.url}") String aiServiceUrl,
            @Value("${ai.sidecar-secret}") String sidecarSecret
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5_000);
        requestFactory.setReadTimeout(20_000);

        return RestClient.builder()
                .baseUrl(aiServiceUrl)
                .defaultHeader("X-Sidecar-Secret", sidecarSecret)
                .requestFactory(requestFactory)
                .build();
    }
}
