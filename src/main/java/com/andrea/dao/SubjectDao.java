package com.andrea.dao;

import com.andrea.dto.AllSubjectDto;
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

    public List<AllSubjectDto> getAllSubjects() {
        List<AllSubjectDto> subjects = new ArrayList<>();
        String getSubject = "SELECT * FROM subject ORDER BY name ASC";
        String getNameTeacher = "SELECT name FROM account WHERE id_account = ?";
        String getNameCourse = "SELECT name FROM course WHERE id_course = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(getSubject);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Integer idSubject = resultSet.getInt("id_subject");
                String name = resultSet.getString("name");
                Integer id_course = resultSet.getInt("id_course");
                Integer id_teacher = resultSet.getInt("id_teacher");

                // Ottieni il nome del docente
                String nameTeacherReturn = "";
                try (PreparedStatement nameTeacherStmt = connection.prepareStatement(getNameTeacher)) {
                    nameTeacherStmt.setInt(1, id_teacher);
                    try (ResultSet rsNameTeacher = nameTeacherStmt.executeQuery()) {
                        if (rsNameTeacher.next()) {
                            nameTeacherReturn = rsNameTeacher.getString("name");
                        }
                    }
                }

                // Ottieni il nome del corso
                String nameCourseReturn = "";
                try (PreparedStatement nameCourseStmt = connection.prepareStatement(getNameCourse)) {
                    nameCourseStmt.setInt(1, id_course);
                    try (ResultSet rsNameCourse = nameCourseStmt.executeQuery()) {
                        if (rsNameCourse.next()) {
                            nameCourseReturn = rsNameCourse.getString("name");
                        }
                    }
                }

                // Crea l'oggetto Subject con tutti i dati
                AllSubjectDto subject = new AllSubjectDto(idSubject, name, id_course, nameCourseReturn, id_teacher, nameTeacherReturn);
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
