package com.andrea.service;

import com.andrea.dao.QuizDao;
import com.andrea.model.Quiz;

import java.util.List;

public class QuizService {
    private QuizDao quizDao = new QuizDao();

    public Quiz addQuiz(Quiz quiz) {
        return quizDao.addQuiz(quiz);
    }

    public List<Quiz> getAllQuizzesByIdSubject(int id_subject) {
        return quizDao.getAllQuizzesByIdSubject(id_subject);
    }

    public Quiz getQuizById(int id_quiz) {
        return quizDao.getQuizById(id_quiz);
    }

    public boolean updateQuiz(Quiz quiz) {
        return quizDao.updateQuiz(quiz);
    }

    public boolean deleteQuiz(int id_quiz) {
        return quizDao.deleteQuiz(id_quiz);
    }

    public List<Quiz> getQuizByAccount(int id_account) {
        return quizDao.getQuizByAccount(id_account);
    }
}
