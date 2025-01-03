package com.andrea.controller;

import com.andrea.model.Question;
import com.andrea.model.Quiz;
import com.andrea.service.QuestionService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class QuestionController {
    private QuestionService questionService = new QuestionService();

    public void registerRoutes(Javalin app) {
        app.post("/question", this::addQuestion);
        app.get("/question", this::getQuestionsByQuizId);
    }

    public void addQuestion(Context ctx) {
        try {
            System.out.println("New Question");

            Question question = null;
            try {
                question = ctx.bodyAsClass(Question.class);
                System.out.println(question.getQuestion());
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            //TODO aggiungere validazione campi body

            Question newQuestion = questionService.addQuestion(question);

            if (newQuestion == null) {
                ctx.status(400).json("Invalid request");
            } else {
                ctx.status(201).json(newQuestion);
            }
        } catch (Exception e) {
            ctx.status(400).json("Error");
        }
    }

    public void getQuestionsByQuizId(Context ctx) {
        System.out.println("Get Question List");

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

        List<Question> list = questionService.getQuestionsByQuizId(id_quiz);

        if (list == null || list.isEmpty()) {
            ctx.status(204);
        } else {
            ctx.status(201).json(list);
        }
    }
}
