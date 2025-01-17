package com.andrea.dao;

import com.andrea.dto.AllSubjectDto;
import com.andrea.dto.SubjectDto;
import com.andrea.exception.NotTeacherException;
import com.andrea.model.Lesson;
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

    public List<AllSubjectDto> getAllSubjectByIdTeacher(int id_teacher) {
        List<AllSubjectDto> subjects = new ArrayList<>();
        String getSubject = "SELECT * FROM subject WHERE id_teacher = ? ORDER BY name ASC";
        String getNameTeacher = "SELECT name FROM account WHERE id_account = ?";
        String getNameCourse = "SELECT name FROM course WHERE id_course = ?";

        try {
            // Prepara la query e imposta il valore del parametro id_teacher
            PreparedStatement stm1 = connection.prepareStatement(getSubject);
            stm1.setInt(1, id_teacher); // Qui imposti il valore del parametro
            ResultSet rs = stm1.executeQuery();

            while (rs.next()) {
                Integer idSubject = rs.getInt("id_subject");
                String name = rs.getString("name");
                Integer id_course = rs.getInt("id_course");

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
            throw new RuntimeException(e);
        }
        return subjects;
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

    public Subject getSubjectById (int id_subject) {
        String findSubject = "SELECT * FROM subject WHERE id_subject = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(findSubject);

            stm.setInt(1, id_subject);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Subject subject = new Subject(rs.getInt("id_subject"), rs.getString("name"), rs.getInt("id_course"), rs.getInt("id_teacher"));
                return subject;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public List<AllSubjectDto> getSubjectByCourse(int id_course) {
        String query = """
            SELECT
                S.id_subject,
                S.name,
                S.id_teacher
            FROM
                Subject S
            WHERE
                S.id_course = ?;
            """;

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id_course);

            ResultSet rs = stm.executeQuery();

            List<AllSubjectDto> list = new ArrayList<>();
            while (rs.next()) {
                AllSubjectDto subject = new AllSubjectDto();
                subject.setId_subject(rs.getInt("id_subject"));
                subject.setName(rs.getString("name"));
                subject.setId_teacher(rs.getInt("id_teacher"));

                list.add(subject);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SubjectDto> getSubjectByAccount(int id_account) {
        List<SubjectDto> list = new ArrayList<>();
        String query = """
                SELECT
                    s.id_subject,
                    s.name AS subject_name
                FROM
                    Enrolled e
                JOIN
                    Course c ON e.id_course = c.id_course
                JOIN
                    Subject s ON s.id_course = c.id_course
                WHERE
                    e.id_account = ?;
                """;
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id_account);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                SubjectDto subject = new SubjectDto();
                subject.setId_subject(rs.getInt("id_subject"));
                subject.setSubject_name(rs.getString("subject_name"));

                list.add(subject);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
