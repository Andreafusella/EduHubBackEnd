package com.andrea.service;

import com.andrea.dao.ScoreDao;
import com.andrea.dto.AccountScoreDto;
import com.andrea.model.Score;

import java.util.List;

public class ScoreService {
    private ScoreDao scoreDao = new ScoreDao();

    public int addScore(Score score) {
        return scoreDao.addScore(score);
    }

    public List<Score> getScoreByAccountQuiz(int id_account, int id_quiz) {
        return scoreDao.getScoreByAccountQuiz(id_account, id_quiz);
    }

    public List<AccountScoreDto> getScore5Account(int id_course) {
        return scoreDao.getScore5Account(id_course);
    }
}
