package com.andrea.dao;

import com.andrea.model.Question;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    // Aggiunge una nuova domanda
    public Question addQuestion(Question question) {
        String query = "INSERT INTO question (question, textA, textB, textC, textD, right_answer, id_quiz) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, question.getQuestion());
            statement.setString(2, question.getTextA());
            statement.setString(3, question.getTextB());
            statement.setString(4, question.getTextC());
            statement.setString(5, question.getTextD());
            statement.setString(6, question.getRight_answer());
            statement.setInt(7, question.getId_quiz());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return new Question(
                                generatedId,
                                question.getQuestion(),
                                question.getTextA(),
                                question.getTextB(),
                                question.getTextC(),
                                question.getTextD(),
                                question.getRight_answer(),
                                question.getId_quiz()
                        );
                    }
                }
            }
            throw new SQLException("Question creation failed, no ID obtained.");
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding question: " + e.getMessage(), e);
        }
    }

    // Recupera tutte le domande associate a un quiz
    public List<Question> getQuestionsByQuizId(int quizId) {
        String query = "SELECT * FROM question WHERE id_quiz = ?";
        List<Question> questions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    questions.add(new Question(
                            resultSet.getInt("id_question"),
                            resultSet.getString("question"),
                            resultSet.getString("textA"),
                            resultSet.getString("textB"),
                            resultSet.getString("textC"),
                            resultSet.getString("textD"),
                            resultSet.getString("right_answer"),
                            resultSet.getInt("id_quiz")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching questions: " + e.getMessage(), e);
        }
        return questions;
    }

    // Aggiorna una domanda esistente
    public boolean updateQuestion(Question question) {
        String query = "UPDATE question SET question = ?, textA = ?, textB = ?, textC = ?, textD = ?, right_answer = ?, id_quiz = ? WHERE id_question = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, question.getQuestion());
            statement.setString(2, question.getTextA());
            statement.setString(3, question.getTextB());
            statement.setString(4, question.getTextC());
            statement.setString(5, question.getTextD());
            statement.setString(6, question.getRight_answer());
            statement.setInt(7, question.getId_quiz());
            statement.setInt(8, question.getId_question());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating question: " + e.getMessage(), e);
        }
    }

    // Elimina una domanda per ID
    public boolean deleteQuestion(int questionId) {
        String query = "DELETE FROM question WHERE id_question = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting question: " + e.getMessage(), e);
        }
    }

    // Recupera una domanda per ID
    public Question getQuestionById(int questionId) {
        String query = "SELECT * FROM question WHERE id_question = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Question(
                            resultSet.getInt("id_question"),
                            resultSet.getString("question"),
                            resultSet.getString("textA"),
                            resultSet.getString("textB"),
                            resultSet.getString("textC"),
                            resultSet.getString("textD"),
                            resultSet.getString("right_answer"),
                            resultSet.getInt("id_quiz")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching question by ID: " + e.getMessage(), e);
        }
        return null;
    }
}