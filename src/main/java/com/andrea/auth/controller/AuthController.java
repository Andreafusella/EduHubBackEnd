package com.andrea.auth.controller;

import com.andrea.auth.dao.AuthDao;
import com.andrea.auth.dto.LoginDto;
import com.andrea.auth.util.JwtUtil;
import com.andrea.exception.ValidationException;
import com.andrea.validator.LoginValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class AuthController {
    private AuthDao authDao = new AuthDao();

    public void registerRoutes(Javalin app) {
        app.post("/login", this::login);
        app.get("/id-user", this::getAccountId);
    }

    public void login(Context ctx) {
        try {
            System.out.println("Login");
            LoginDto loginDto = null;

            try {
                loginDto = ctx.bodyAsClass(LoginDto.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            LoginValidator.validate(loginDto);
            List<Object> listReturn = authDao.findUser(loginDto);

            if (listReturn == null) {
                ctx.status(401).json(Map.of("message", "Invalid credentials"));
                return;
            }

            if ((boolean) listReturn.get(1)) {
                String role = (String) listReturn.get(2);
                int id_account = (int) listReturn.get(0);
                String token = JwtUtil.generateToken(id_account, role);
                Map<String, Object> response = Map.of("token", new TokenResponse(token), "role", listReturn);
                ctx.status(201).json(response);
            } else {
                ctx.status(401).json(Map.of("message", "Invalid credentials"));
            }
        } catch (ValidationException e) {
            ctx.status(400).json(e.getMessage());
        }
    }

    public void getAccountId(Context ctx) {

        String idAccount = ctx.attribute("id_account");
        if (idAccount == null) {
            ctx.status(401).json(Map.of("message", "Unauthorized"));
            return;
        }
        ctx.status(200).json(Map.of("id_account", idAccount));
    }


    private static class TokenResponse {
        public final String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}
