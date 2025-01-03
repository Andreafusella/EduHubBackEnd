package com.andrea.controller;

import com.andrea.model.Score;
import com.andrea.service.ScoreService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class ScoreController {
    private ScoreService scoreService = new ScoreService();

    public void registerRoutes(Javalin app) {
        app.get("/score", this::getScoreByAccountQuiz);
        app.post("/score", this::addScore);
    }

    public void addScore(Context ctx) {
        System.out.println("Add Score");

        Score score = ctx.bodyAsClass(Score.class);

        int isCreated = scoreService.addScore(score);
        if (isCreated == 1) {
            ctx.status(201);
        } else {
            ctx.status(401);
        }
    }

    public void getScoreByAccountQuiz(Context ctx) {
        System.out.println("Get Score");

        String idQuizParam = ctx.queryParam("id_quiz");
        String idAccountParam = ctx.queryParam("id_account");

        if (idQuizParam == null || idQuizParam.isEmpty() || idAccountParam == null || idAccountParam.isEmpty()) {
            ctx.status(400).json("Missing parameter.");
            return;
        }

        int id_quiz = 0;
        int id_account = 0;
        try {
            id_quiz = Integer.parseInt(idQuizParam);
            id_account = Integer.parseInt(idAccountParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid parameter: it must be an integer.");
            return;
        }

        List<Score> score = scoreService.getScoreByAccountQuiz(id_account, id_quiz);

        if (score == null || score.isEmpty()) {
            ctx.status(204);
        } else {
            ctx.status(201).json(score);
        }
    }
}
