package net.zenebris.server.utils;

import io.jooby.Context;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class Auth {
    public static final SecureRandom secureRandom = new SecureRandom();
    public static final String SESSION_AUTH_TOKEN = "session_auth_token";


    public static String secureToken() {
        byte[] bytes = new byte[27];
        secureRandom.nextBytes(bytes);
        return new String(Base64.getEncoder().encode(bytes))
                .replace("=", "");
    }

    public static String uniqueToken() {
        return UUID.randomUUID().toString();
    }

    public static String getAuthToken(Context ctx) {
        return ctx.session().get(SESSION_AUTH_TOKEN).value();
    }

    public static String setAuthToken(Context ctx) {
        return setAuthToken(ctx, secureToken());
    }

    public static String setAuthToken(Context ctx, String token) {
        ctx.session().put(SESSION_AUTH_TOKEN, token);
        return token;
    }

}
