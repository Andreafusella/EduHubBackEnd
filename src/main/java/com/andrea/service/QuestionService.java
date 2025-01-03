package com.andrea.service;

import com.andrea.dao.QuestionDao;
import com.andrea.model.Question;

import java.util.List;

public class QuestionService {
    private QuestionDao questionDao = new QuestionDao();

    public Question addQuestion(Question question) {
        return questionDao.addQuestion(question);
    }

    public List<Question> getQuestionsByQuizId(int id_quiz) {
        return questionDao.getQuestionsByQuizId(id_quiz);
    }
}
