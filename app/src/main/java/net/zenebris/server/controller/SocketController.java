package net.zenebris.server.controller;

import io.jooby.Context;
import io.jooby.StatusCode;
import io.jooby.WebSocketConfigurer;

public class SocketController {
    public void socket(Context ctx, WebSocketConfigurer wsc) {
        ctx.setResponseCode(StatusCode.NOT_IMPLEMENTED);
    }
}
