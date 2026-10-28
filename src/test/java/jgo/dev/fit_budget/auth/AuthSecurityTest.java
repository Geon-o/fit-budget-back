package jgo.dev.fit_budget.auth;

import jgo.dev.fit_budget.auth.dto.UserResponse;
import jgo.dev.fit_budget.config.SecurityConfig;
import jgo.dev.fit_budget.ledger.LedgerAccessGuard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtProvider.class})
@TestPropertySource(properties = "app.jwt.secret=test-secret-test-secret-test-secret-123")
class AuthSecurityTest {

    @Autowired MockMvc mvc;
    @Autowired JwtProvider jwt;
    @MockitoBean AuthService authService;
    @MockitoBean LedgerAccessGuard ledgerAccessGuard; // MvcConfig의 인터셉터가 요구

    @Test
    void meRequiresValidAccessToken() throws Exception {
        when(authService.getCurrentUser(any())).thenReturn(new UserResponse(null, "n", "nick"));

        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + jwt.issueRefresh(UUID.randomUUID())))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + jwt.issueAccess(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("nick"));
    }
}
