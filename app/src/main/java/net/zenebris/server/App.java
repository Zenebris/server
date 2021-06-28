package net.zenebris.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jooby.Jooby;
import io.jooby.ServerOptions;
import io.jooby.json.JacksonModule;
import io.lettuce.core.RedisClient;
import io.minio.MinioClient;
import net.zenebris.server.controller.SocketController;
import net.zenebris.server.controller.UserController;
import net.zenebris.server.middleware.AuthMiddleware;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class App extends Jooby {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private final SessionFactory dataSource;
    private final RedisClient redisClient;
    private final MinioClient minioClient;

    public App(SessionFactory dataSource, RedisClient redisClient, MinioClient minioClient) {
        this.dataSource = dataSource;
        this.redisClient = redisClient;
        this.minioClient = minioClient;

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
        runApp(args, App::create);
    }

    public static App create() {
        return new App(getSqlDatasource(), getRedisClient(), getMinioClient());
    }

    private static SessionFactory getSqlDatasource() {
        Map<String, String> env = getEnv();
        String host = env.getOrDefault("ZENEBRIS_SQL_HOST", "localhost");
        String db = env.getOrDefault("ZENEBRIS_SQL_DATABASE", "zenebris");
        String user = env.getOrDefault("ZENEBRIS_SQL_USER", "ZenebrisPostgres");
        String password = env.get("ZENEBRIS_SQL_PASSWORD");
        if (password == null)
            throw new IllegalStateException("Environment ZENEBRIS_SQL_PASSWORD must be defined.");

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.provider", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        configuration.setProperty(Environment.USER, user);
        configuration.setProperty(Environment.PASS, password);
        configuration.setProperty(Environment.URL, "jdbc:postgresql://" + host + "/" + db);
        configuration.setProperty(Environment.HBM2DDL_AUTO, "create-only");

        return configuration
                .buildSessionFactory();
    }

    private static RedisClient getRedisClient() {
        Map<String, String> env = getEnv();
        String host = env.getOrDefault("ZENEBRIS_REDIS_HOST", "localhost");
        String database = env.getOrDefault("ZENEBRIS_REDIS_DATABASE", "0");

        String uri = "redis://" + host + "/" + database;
        logger.debug("Connecting redis to " + uri);
        return RedisClient.create(uri);
    }

    private static MinioClient getMinioClient() {
        Map<String, String> env = getEnv();
        String host = env.getOrDefault("ZENEBRIS_MINIO_HOST", "localhost");
        String key = env.get("ZENEBRIS_MINIO_KEY");
        String secret = env.get("ZENEBRIS_MINIO_SECRET");

        if (secret == null)
            throw new IllegalStateException("Environment ZENEBRIS_MINIO_SECRET must be defined.");

        if (key == null)
            throw new IllegalStateException("Environment ZENEBRIS_MINIO_KEY must be defined.");

        String endpoint = "http://" + host;
        logger.debug("Connecting minio to " + endpoint);
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(key, secret)
                .build();
    }

    private static Map<String, String> getEnv() {
        return System.getenv();
    }
}
