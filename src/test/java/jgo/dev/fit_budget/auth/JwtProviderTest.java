package jgo.dev.fit_budget.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtProviderTest {

    private final JwtProvider provider = new JwtProvider("test-secret-test-secret-test-secret-123");

    @Test
    void accessAndRefreshAreNotInterchangeable() {
        String access = provider.issueAccess("a@b.com");
        String refresh = provider.issueRefresh("a@b.com");

        assertEquals("a@b.com", provider.verify(access, JwtProvider.ACCESS).orElseThrow());
        assertEquals("a@b.com", provider.verify(refresh, JwtProvider.REFRESH).orElseThrow());
        assertTrue(provider.verify(refresh, JwtProvider.ACCESS).isEmpty());
        assertTrue(provider.verify(access, JwtProvider.REFRESH).isEmpty());
    }

    @Test
    void rejectsGarbageAndForeignSignature() {
        assertTrue(provider.verify("garbage", JwtProvider.ACCESS).isEmpty());
        String foreign = new JwtProvider("other-secret-other-secret-other-secret-1").issueAccess("a@b.com");
        assertTrue(provider.verify(foreign, JwtProvider.ACCESS).isEmpty());
    }

    @Test
    void rejectsShortSecret() {
        assertThrows(IllegalStateException.class, () -> new JwtProvider("short"));
    }
}
