package com.andrea.service;

import com.andrea.dao.QuestionDao;
import com.andrea.model.Question;

public class QuestionService {
    private QuestionDao questionDao = new QuestionDao();

    public Question addQuestion(Question question) {
        return questionDao.addQuestion(question);
    }
}
