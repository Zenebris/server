package net.zenebris.server;

import io.jooby.MockContext;
import io.jooby.MockRouter;
import io.jooby.StatusCode;
import net.zenebris.server.model.Login;
import net.zenebris.server.model.Register;
import org.junit.Assert;
import org.junit.Test;

public class UserControllerTest {
    private final MockRouter router = new MockRouter(new App());

    @Test
    public void testHandleuserLoginPost() {
        MockContext context = new MockContext();
        context.setBody("{}");
        context.setBodyObject(new Login());

        router.post("/api/v1/user/login", context, response -> {
            Assert.assertEquals(StatusCode.NOT_IMPLEMENTED, response.getStatusCode());
        });
    }
    
    @Test
    public void testHandleuserRegisterPost() {
        MockContext context = new MockContext();
        context.setBody("{}");
        context.setBodyObject(new Register());

        router.post("/api/v1/user/register", context, response -> {
            Assert.assertEquals(StatusCode.NOT_IMPLEMENTED, response.getStatusCode());
        });
    }
    
}