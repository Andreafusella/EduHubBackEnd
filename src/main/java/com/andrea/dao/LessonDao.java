package com.andrea.dao;

import com.andrea.exception.NotSubjectException;
import com.andrea.model.Lesson;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LessonDao {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    //TODO aggiungere eccezione per lezione gia esistente
    public Lesson addLesson(Lesson lessons) throws NotSubjectException {
        String addLesson = "INSERT INTO lesson (id_course, id_subject, lesson_date, hour_start, hour_end, classroom, title, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String checkSubjectQuery = "SELECT 1 FROM subject WHERE id_subject = ?";

        try {
            PreparedStatement checkSubject = connection.prepareStatement(checkSubjectQuery);
            checkSubject.setInt(1, lessons.getId_subject());
            ResultSet rs = checkSubject.executeQuery();


            if (!rs.next()) {
                throw new NotSubjectException("Subject with id " + lessons.getId_subject() + " not found.");
            }


            PreparedStatement newLesson = connection.prepareStatement(addLesson, Statement.RETURN_GENERATED_KEYS);

            newLesson.setInt(1, lessons.getId_course());
            newLesson.setInt(2, lessons.getId_subject());
            newLesson.setObject(3, lessons.getLesson_date());
            newLesson.setTime(4, java.sql.Time.valueOf(lessons.getHour_start()));
            newLesson.setTime(5, java.sql.Time.valueOf(lessons.getHour_end()));
            newLesson.setString(6, lessons.getClassroom());
            newLesson.setString(7, lessons.getTitle());
            newLesson.setString(8, lessons.getDescription());

            int affectedRows = newLesson.executeUpdate();
            int generatedId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newLesson.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Lesson creation failed, no rows added");
            }


            return new Lesson(generatedId, lessons.getId_course(), lessons.getId_subject(), lessons.getTitle(), lessons.getDescription(), lessons.getLesson_date(), lessons.getHour_start(), lessons.getHour_end(), lessons.getClassroom());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteLesson(int id_lesson) {
        String removeLesson = "DELETE FROM lesson WHERE id_lesson = ?";

        try {
            PreparedStatement deleteLesson = connection.prepareStatement(removeLesson);
            deleteLesson.setInt(1, id_lesson);

            int affectedRows = deleteLesson.executeUpdate();

            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting lesson with id " + id_lesson, e);
        }
    }

    public Lesson updateLesson(Lesson lessons) {
        String updateLesson = """
        UPDATE lesson
        SET id_course = ?, lesson_date = ?, hour_start = ?, hour_end = ?, classroom = ?, title = ?, description = ?
        WHERE id_lesson = ?;
    """;

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateLesson);


            updateStatement.setInt(1, lessons.getId_course());
            updateStatement.setObject(2, lessons.getLesson_date());
            updateStatement.setTime(3, java.sql.Time.valueOf(lessons.getHour_start()));
            updateStatement.setTime(4, java.sql.Time.valueOf(lessons.getHour_end()));
            updateStatement.setString(5, lessons.getClassroom());
            updateStatement.setString(6, lessons.getTitle());
            updateStatement.setString(7, lessons.getDescription());
            updateStatement.setInt(8, lessons.getId_lesson());

            int affectedRows = updateStatement.executeUpdate();

            if (affectedRows > 0) {

                return getLessonById(lessons.getId_lesson());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating lesson with id " + lessons.getId_lesson(), e);
        }
    }

    public Lesson getLessonById(int id_lesson) {
        String getLessonQuery = "SELECT * FROM lesson WHERE id_lesson = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLessonQuery)) {
            preparedStatement.setInt(1, id_lesson);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return new Lesson(
                        resultSet.getInt("id_lesson"),
                        resultSet.getInt("id_course"),
                        resultSet.getInt("id_subject"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("lesson_date").toLocalDate(),
                        resultSet.getTime("hour_start").toLocalTime(),
                        resultSet.getTime("hour_end").toLocalTime(),
                        resultSet.getString("classroom")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lesson with id " + id_lesson, e);
        }
    }

    public List<Lesson> getLessonsByCourseId(int courseId) {
        String getLessonsQuery = """
            SELECT *
            FROM lesson
            WHERE id_course = ?
            ORDER BY lesson_date;""";

        List<Lesson> lessons = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLessonsQuery)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesson lesson = new Lesson();
                lesson.setId_lesson(resultSet.getInt("id_lesson"));
                lesson.setId_course(resultSet.getInt("id_course"));
                lesson.setLesson_date(resultSet.getObject("lesson_date", LocalDate.class));
                lesson.setHour_start(resultSet.getTime("hour_start").toLocalTime());
                lesson.setHour_end(resultSet.getTime("hour_end").toLocalTime());
                lesson.setClassroom(resultSet.getString("classroom"));
                lesson.setTitle(resultSet.getString("title"));
                lesson.setDescription(resultSet.getString("description"));
                lesson.setId_subject(resultSet.getInt("id_subject"));

                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lessons for course with id " + courseId, e);
        }

        return lessons;
    }

}