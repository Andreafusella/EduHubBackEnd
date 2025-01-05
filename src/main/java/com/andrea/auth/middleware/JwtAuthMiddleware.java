package com.andrea.auth.middleware;

import com.andrea.auth.util.JwtUtil;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class JwtAuthMiddleware implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        System.out.println("Entra nel middleware");

        String authHeader = ctx.header("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Token non trovato o malformato");
            ctx.status(401).result("Missing or invalid token");
            return;
        }

        String token = authHeader.replace("Bearer ", "");
        try {
            String id_account = JwtUtil.validateToken(token);
            ctx.attribute("id_account", id_account);
            System.out.println("Token valido per id_account: " + id_account);

        } catch (Exception e) {
            System.out.println("Token non valido o scaduto");
            ctx.status(401).result("Invalid or expired token");

        }
    }
}