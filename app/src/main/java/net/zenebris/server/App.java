package net.zenebris.server;

import io.jooby.Jooby;

public class App extends Jooby {
    public App() {
    }

    public static void main(String[] args) {
        runApp(args, () -> new App());
    }
}
