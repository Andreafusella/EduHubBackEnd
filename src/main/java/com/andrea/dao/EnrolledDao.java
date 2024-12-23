package com.andrea.dao;

import com.andrea.dto.RemoveEnrolledDto;
import com.andrea.exception.AccountNotFoundException;
import com.andrea.exception.CourseNotFoundException;
import com.andrea.exception.EnrolledExistException;
import com.andrea.exception.EnrolledNotExistException;
import com.andrea.model.Course;
import com.andrea.model.Enrolled;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrolledDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void addEnrolled(Enrolled enrolled) throws EnrolledExistException, AccountNotFoundException, CourseNotFoundException {
        String addEnrolled = "INSERT INTO enrolled (id_account, id_course, enrollment_date) VALUES (?, ?, ?)";
        String checkEnrolledExist = "SELECT COUNT(*) FROM enrolled WHERE id_account = ? AND id_course = ?";
        String checkAccountExist = "SELECT COUNT(*) FROM Account WHERE id_account = ?";
        String checkCourseExist = "SELECT COUNT(*) FROM Course WHERE id_course = ?";

        try {

            //account not exist
            PreparedStatement checkAccount = connection.prepareStatement(checkAccountExist);
            checkAccount.setInt(1, enrolled.getId_account());
            ResultSet accountResult = checkAccount.executeQuery();
            if (accountResult.next() && accountResult.getInt(1) == 0) {
                throw new AccountNotFoundException("Account with id " + enrolled.getId_account() + " does not exist.");
            }

            //course not exist
            PreparedStatement checkCourse = connection.prepareStatement(checkCourseExist);
            checkCourse.setInt(1, enrolled.getId_course());
            ResultSet courseResult = checkCourse.executeQuery();
            if (courseResult.next() && courseResult.getInt(1) == 0) {
                throw new CourseNotFoundException("Course with id " + enrolled.getId_course() + " does not exist.");
            }

            //enrolled already exist
            PreparedStatement checkEnrolled = connection.prepareStatement(checkEnrolledExist);
            checkEnrolled.setInt(1, enrolled.getId_account());
            checkEnrolled.setInt(2, enrolled.getId_course());

            ResultSet rs = checkEnrolled.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Enrolled already exist");
                throw new EnrolledExistException("Enrolled already exist");
            }

            //create enrolled
            PreparedStatement newEnrolled = connection.prepareStatement(addEnrolled);
            newEnrolled.setInt(1, enrolled.getId_account());
            newEnrolled.setInt(2, enrolled.getId_course());
            newEnrolled.setObject(3, enrolled.getEnrollment_date());

            int affectedRows = newEnrolled.executeUpdate();

            //TODO: aggiungere invio email allo studente
            if (affectedRows < 0) {
                throw new SQLException("Course creation failed, no rows added");
            }

            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int removeEnrolled(RemoveEnrolledDto enrolled) throws EnrolledNotExistException {
        String checkEnrolled = "SELECT COUNT(*) FROM enrolled WHERE id_account = ? AND id_course = ?";
        String deleteEnrolled = "DELETE FROM Enrolled WHERE id_account = ? AND id_course = ?";

        try {

            //check enrolled exist
            PreparedStatement enrolledExist = connection.prepareStatement(checkEnrolled);
            enrolledExist.setInt(1, enrolled.getId_account());
            enrolledExist.setInt(2, enrolled.getId_course());

            ResultSet rs = enrolledExist.executeQuery();

            if (!(rs.next() && rs.getInt(1) > 0)) {
                System.out.println("Enrolled not exist");
                throw new EnrolledNotExistException("Enrolled not exist");
            }

            //delete enrolled
            PreparedStatement enrolledDelete = connection.prepareStatement(deleteEnrolled);
            enrolledDelete.setInt(1, enrolled.getId_account());
            enrolledDelete.setInt(2, enrolled.getId_course());

            int affectedRows = enrolledDelete.executeUpdate();

            if (affectedRows > 0) {
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getAllEnrolledCourse(int id_account) {
        List<Course> courseList = new ArrayList<>();
        String enrolledAccount = """
                SELECT
                    course.id_course,
                    course.name,
                    course.description,
                    course.date_start,
                    course.date_finish
                FROM
                    Enrolled enrolled
                INNER JOIN
                    Course course
                ON
                    enrolled.id_course = course.id_course
                WHERE
                    enrolled.id_account = ?;
                """;

        //find enrolled course with id_account
        try {
            PreparedStatement courseStm = connection.prepareStatement(enrolledAccount);
            courseStm.setInt(1, id_account);

            ResultSet rs = courseStm.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setId_course(rs.getInt("id_course"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setDate_start(rs.getObject("date_start", LocalDate.class));
                course.setDate_finish(rs.getObject("date_finish", LocalDate.class));

                courseList.add(course);
            }

            if (courseList.isEmpty() || courseList == null) {
                return null;
            } else {
                return courseList;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
