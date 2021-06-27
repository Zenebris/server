package net.zenebris.server.middleware;

import io.jooby.MockContext;
import net.zenebris.server.utils.Auth;
import org.junit.Test;

public class AuthMiddlewareTest {
    @Test
    public void testAuthMiddleware() throws Exception {
        // Over header (recommended)
        MockContext mockContext = new MockContext();
        String value = Auth.secureToken();
        mockContext.session().put(Auth.SESSION_AUTH_TOKEN, value);
        mockContext.setRequestHeader(AuthMiddleware.AUTH_HEADER, value);
        new AuthMiddleware()
                .apply(ctx -> new Object()) // dummy handler
                .apply(mockContext);

        // Over query
        mockContext = new MockContext();
        value = Auth.secureToken();
        mockContext.session().put(Auth.SESSION_AUTH_TOKEN, value);
        mockContext.setQueryString("?" + AuthMiddleware.AUTH_HEADER + "=" + value);
        new AuthMiddleware()
                .apply(ctx -> new Object()) // dummy handler
                .apply(mockContext);
    }
}