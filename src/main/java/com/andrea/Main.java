package com.andrea;

import com.andrea.auth.controller.AuthController;
import com.andrea.controller.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {

            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(8000);

        AccountController accountController = new AccountController();
        AuthController authController = new AuthController();
        CourseController courseController = new CourseController();
        EnrolledController enrolledController = new EnrolledController();
        SubjectController subjectController = new SubjectController();
        LessonController lessonController = new LessonController();
        QuestionController questionController = new QuestionController();
        QuizController quizController = new QuizController();
        PresenceController presenceController = new PresenceController();
        ScoreController scoreController = new ScoreController();
        FileController fileController = new FileController();


        accountController.registerRoutes(app);
        authController.registerRoutes(app);
        courseController.registerRoutes(app);
        enrolledController.registerRoutes(app);
        subjectController.registerRoutes(app);
        lessonController.registerRoutes(app);
        questionController.registerRoutes(app);
        quizController.registerRoutes(app);
        presenceController.registerRoutes(app);
        scoreController.registerRoutes(app);
        fileController.registerRoutes(app);

        System.out.println("Server in running on port 8000");
    }
}