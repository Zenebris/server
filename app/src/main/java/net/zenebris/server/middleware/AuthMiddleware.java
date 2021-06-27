package net.zenebris.server.middleware;

import io.jooby.Route;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import net.zenebris.server.utils.Auth;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AuthMiddleware implements Route.Decorator {
    public static final String AUTH_HEADER = "X-Action-Token";

    @NotNull
    @Override
    public Route.Handler apply(@NotNull Route.Handler next) {
        return ctx -> {
            Optional<String> hAuthToken = ctx.header().get(AUTH_HEADER).toOptional();
            String authToken = hAuthToken.orElseGet(ctx.query().get(AUTH_HEADER)::value);

            if (!authToken.equals(Auth.getAuthToken(ctx)))
                throw new StatusCodeException(StatusCode.UNAUTHORIZED);
            return next.apply(ctx);
        };
    }
}
