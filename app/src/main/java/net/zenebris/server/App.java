package net.zenebris.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jooby.Jooby;
import io.jooby.ServerOptions;
import io.jooby.json.JacksonModule;
import net.zenebris.server.controller.SocketController;
import net.zenebris.server.controller.UserController;
import net.zenebris.server.middleware.AuthMiddleware;

public class App extends Jooby {
    public App() {
        setServerOptions(new ServerOptions()
                .setHttp2(true)
                .setCompressionLevel(5)
                .setPort(8080));

        ObjectMapper objectMapper = JacksonModule.create()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        install(new JacksonModule(objectMapper));

        AuthMiddleware authMiddleware = new AuthMiddleware();
        SocketController socketController = new SocketController();
        UserController userController = new UserController();
        path("api/v1", () -> {
            routes(() -> {
                decorator(authMiddleware);
                ws("/socket", socketController::socket);
            });
            post("/user/login", userController::userLoginPost);
            post("/user/register", userController::userRegisterPost);
        });
    }

    public static void main(String[] args) {
        runApp(args, () -> new App());
    }
}
