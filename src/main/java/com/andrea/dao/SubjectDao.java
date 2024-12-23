package com.andrea.dao;

import com.andrea.exception.NotTeacherException;
import com.andrea.model.Subject;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Subject addSubject(Subject subject) throws NotTeacherException {
        String addSubject = "INSERT INTO subject (name, id_course, id_teacher) VALUES (?, ?, ?)";
        String checkTeacherRole = "SELECT role FROM account WHERE id_account = ?";

        //TODO controllo se l'id teacher corrisponde ad un teacher
        try {
            PreparedStatement checkTeacher = connection.prepareStatement(checkTeacherRole);
            checkTeacher.setInt(1, subject.getId_teacher());
            ResultSet resultSet = checkTeacher.executeQuery();

            if (!resultSet.next() || !"Teacher".equals(resultSet.getString("role"))) {
                throw new NotTeacherException("The provided teacher ID does not correspond to a teacher role.");
            }


            PreparedStatement newSubject = connection.prepareStatement(addSubject, Statement.RETURN_GENERATED_KEYS);

            newSubject.setString(1, subject.getName());
            newSubject.setInt(2, subject.getId_course());
            newSubject.setInt(3, subject.getId_teacher());

            int affectedRows = newSubject.executeUpdate();
            int generateId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newSubject.getGeneratedKeys()){
                    if (generatedKeys.next()) {
                        generateId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Course creation failed, no rows added");
            }

            Subject newSubjectReturn = new Subject(generateId, subject.getName(), subject.getId_course(), subject.getId_teacher());

            return newSubjectReturn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subject";

        try (

             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer idSubject = resultSet.getInt("id_subject");
                String name = resultSet.getString("name");
                Integer idCourse = resultSet.getInt("id_course");
                Integer idTeacher = resultSet.getInt("id_teacher");

                Subject subject = new Subject(idSubject, name, idCourse, idTeacher);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching subjects", e);
        }

        return subjects;
    }

    public boolean deleteSubject(int id_subject) {
        String query = "DELETE FROM subject WHERE id_subject = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_subject);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting subject", e);
        }
    }
}
