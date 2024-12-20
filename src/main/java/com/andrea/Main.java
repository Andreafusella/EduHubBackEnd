package com.andrea;

import com.andrea.auth.controller.AuthController;
import com.andrea.controller.AccountController;
import com.andrea.controller.CourseController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }).start(8000);

        AccountController accountController = new AccountController();
        AuthController authController = new AuthController();
        CourseController courseController = new CourseController();

        accountController.registerRoutes(app);
        authController.registerRoutes(app);
        courseController.registerRoutes(app);

        System.out.println("Server in running on port 8000");
    }
}