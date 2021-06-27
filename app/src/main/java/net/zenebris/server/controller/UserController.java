package net.zenebris.server.controller;

import io.jooby.*;
import net.zenebris.server.model.Login;
import net.zenebris.server.model.Register;
import net.zenebris.server.model.Token;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class UserController { 
    /**
    * 
    * Param body (Login)
    **/
    @NotNull
    public Token userLoginPost(Context ctx) {
        Login body = ctx.body().to(Login.class);
        ctx.setResponseCode(StatusCode.NOT_IMPLEMENTED);
        return new Token();
    }


    /**
    * 
    * Param body (Register)
    **/
    @NotNull
    public Object userRegisterPost(Context ctx) {
        Register body = ctx.body().to(Register.class);
        ctx.setResponseCode(StatusCode.NOT_IMPLEMENTED);
        return new Object();
    }

}
