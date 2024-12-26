package com.andrea.controller;

import com.andrea.model.Question;
import com.andrea.model.Quiz;
import com.andrea.service.QuestionService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class QuestionController {
    private QuestionService questionService = new QuestionService();

    public void registerRoutes(Javalin app) {
        app.post("/question", this::addQuestion);
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
}
