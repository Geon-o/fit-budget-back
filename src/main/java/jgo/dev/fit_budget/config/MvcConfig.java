package jgo.dev.fit_budget.config;

import jgo.dev.fit_budget.ledger.LedgerAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final LedgerAccessInterceptor ledgerAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ledgerAccessInterceptor)
                .addPathPatterns("/api/ledgers/*/**");
    }
}
