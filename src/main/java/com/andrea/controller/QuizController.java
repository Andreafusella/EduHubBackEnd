package com.andrea.controller;

import com.andrea.model.Quiz;
import com.andrea.service.QuizService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class QuizController {
    private QuizService quizService = new QuizService();

    public void registerRoutes(Javalin app) {
        app.post("/quiz", this::addQuiz);
        app.get("/quiz-by-subject", this::getAllQuizzesByIdSubject);
        app.delete("/quiz", this::deleteQuiz);
        app.get("/quiz-by-id", this::getQuizById);
    }

    public void addQuiz(Context ctx) {
        try {
            System.out.println("New Quiz");

            Quiz quiz = null;
            try {
                quiz = ctx.bodyAsClass(Quiz.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            //TODO aggiungere validazione campi body

            Quiz newQuiz = quizService.addQuiz(quiz);

            if (newQuiz == null) {
                ctx.status(400).json("Invalid request");
            } else {
                ctx.status(201).json(newQuiz);
            }
        } catch (Exception e) {
            ctx.status(400).json("Error");
        }
    }

    public void getAllQuizzesByIdSubject(Context ctx) {

        System.out.println("Get All Quiz");

        String idSubjectParam = ctx.queryParam("id_subject");

        if (idSubjectParam == null || idSubjectParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_subject' parameter.");
            return;
        }

        try {
            Integer id_subject = Integer.parseInt(idSubjectParam.trim());
            System.out.println(id_subject);

            List<Quiz> quizList = quizService.getAllQuizzesByIdSubject(id_subject);

            if (quizList == null || quizList.isEmpty()) {
                ctx.status(204);
            } else {
                ctx.status(200).json(quizList);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_subject' parameter. It must be an integer.");
        }
    }

    public void getQuizById(Context ctx) {
        try {
            System.out.println("Get Quiz By id");

            String idQuizParam = ctx.queryParam("id_quiz");

            if (idQuizParam == null || idQuizParam.isEmpty()) {
                ctx.status(400).json("Missing 'id_quiz' parameter.");
                return;
            }

            int id_quiz = 0;
            try {
                id_quiz = Integer.parseInt(idQuizParam);
            } catch (NumberFormatException e) {
                ctx.status(400).json("Invalid 'id_quiz' parameter: it must be an integer.");
                return;
            }

            Quiz quiz = quizService.getQuizById(id_quiz);

            if (quiz == null) {
                ctx.status(204).json("No account found with id: " + id_quiz);
            } else {
                ctx.status(200).json(quiz);
            }
        } catch (Exception e) {
            ctx.status(500).json("Unexpected error occurred while deleting quiz.");
        }
    }

    public void deleteQuiz(Context ctx) {
        try {
            System.out.println("Delete Quiz");

            String idQuizParam = ctx.queryParam("id_quiz");

            if (idQuizParam == null || idQuizParam.isEmpty()) {
                ctx.status(400).json("Missing 'id_quiz' parameter.");
                return;
            }

            int id_quiz = 0;
            try {
                id_quiz = Integer.parseInt(idQuizParam);
            } catch (NumberFormatException e) {
                ctx.status(400).json("Invalid 'id_quiz' parameter: it must be an integer.");
                return;
            }

            boolean delete = quizService.deleteQuiz(id_quiz);

            if (delete) {
                ctx.status(201).json("Quiz delete");
            } else if (!delete) {
                ctx.status(404).json("No quiz record found to delete.");
            } else {
                ctx.status(500).json("Unexpected error occurred while deleting quiz.");
            }
        } catch (Exception e) {
            ctx.status(500).json("Unexpected error occurred while deleting quiz.");
        }
    }
}
