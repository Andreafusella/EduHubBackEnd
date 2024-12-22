package com.andrea.dao;

import com.andrea.dto.RemoveEnrolled;
import com.andrea.exception.AccountNotFoundException;
import com.andrea.exception.CourseNotFoundException;
import com.andrea.exception.EnrolledExistException;
import com.andrea.exception.EnrolledNotExistException;
import com.andrea.model.Enrolled;
import com.andrea.utility.database.DatabaseConnection;
import com.andrea.validator.EnrolledDeleteValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public int removeEnrolled(RemoveEnrolled enrolled) throws EnrolledNotExistException {
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
}
