package net.zenebris.server.utils;

import io.jooby.MockContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class AuthTest {

    @Test
    public void secureToken() {
        Set<String> alreadyGenerated = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            String token = Auth.secureToken();
            Assert.assertEquals(token.length(), 36);
            Assert.assertTrue(alreadyGenerated.add(token));
        }
    }

    @Test
    public void uniqueToken() {
        Set<String> alreadyGenerated = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            String token = Auth.uniqueToken();
            Assert.assertEquals(token.length(), 36);
            Assert.assertTrue(alreadyGenerated.add(token));
        }
    }

    @Test
    public void testAuthToken() {
        MockContext context = new MockContext();
        String token = Auth.setAuthToken(context);
        Assert.assertEquals(token, Auth.getAuthToken(context));
    }
}