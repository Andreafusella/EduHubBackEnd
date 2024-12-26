package com.andrea.dao;

import com.andrea.model.Quiz;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    // Aggiunge un nuovo quiz
    public Quiz addQuiz(Quiz quiz) {
        String query = "INSERT INTO quiz (title, description, date, id_subject) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, quiz.getTitle());
            statement.setString(2, quiz.getDescription());
            statement.setObject(3, quiz.getQuiz_date());
            statement.setInt(4, quiz.getId_subject());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return new Quiz(
                                generatedId,
                                quiz.getTitle(),
                                quiz.getDescription(),
                                quiz.getQuiz_date(),
                                quiz.getId_subject()
                        );
                    }
                }
            }
            throw new SQLException("Quiz creation failed, no ID obtained.");
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding quiz: " + e.getMessage(), e);
        }
    }

    // Recupera tutti i quiz
    public List<Quiz> getAllQuizzesByIdSubject(int id_subject) {
        String query = "SELECT * FROM quiz WHERE id_subject = ? ORDER BY id_quiz DESC";
        List<Quiz> quizzes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_subject);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setId_quiz(resultSet.getInt("id_quiz"));
                    quiz.setTitle( resultSet.getString("title"));
                    quiz.setDescription(resultSet.getString("description"));
                    quiz.setQuiz_date(resultSet.getObject("date", LocalDate.class));
                    quiz.setId_subject(resultSet.getInt("id_subject"));

                    quizzes.add(quiz);
                }
                if (quizzes == null || quizzes.isEmpty()) {
                    return null;
                } else {
                    return quizzes;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all quizzes: " + e.getMessage(), e);
        }

    }

    // Recupera un quiz per ID
    public Quiz getQuizById(int quizId) {
        String query = "SELECT * FROM quiz WHERE id_quiz = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Quiz(
                            resultSet.getInt("id_quiz"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getObject("date", java.sql.Date.class).toLocalDate(),
                            resultSet.getInt("id_subject")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching quiz by ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Aggiorna un quiz esistente
    public boolean updateQuiz(Quiz quiz) {
        String query = "UPDATE quiz SET title = ?, description = ?, date = ?, id_subject = ? WHERE id_quiz = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, quiz.getTitle());
            statement.setString(2, quiz.getDescription());
            statement.setObject(3, quiz.getQuiz_date());
            statement.setInt(4, quiz.getId_subject());
            statement.setInt(5, quiz.getId_quiz());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating quiz: " + e.getMessage(), e);
        }
    }

    // Elimina un quiz per ID
    public boolean deleteQuiz(int id_quiz) {
        String query = "DELETE FROM quiz WHERE id_quiz = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_quiz);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting quiz: " + e.getMessage(), e);
        }
    }
}