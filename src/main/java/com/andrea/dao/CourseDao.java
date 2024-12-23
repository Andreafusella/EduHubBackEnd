package com.andrea.dao;

import com.andrea.dto.NewCourseDto;
import com.andrea.model.Course;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Course addCourse(NewCourseDto courseDto) {
        String addCourse = "INSERT INTO course (name, description, date_start, date_finish) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement newCourse = connection.prepareStatement(addCourse, Statement.RETURN_GENERATED_KEYS);

            newCourse.setString(1, courseDto.getName());
            newCourse.setString(2, courseDto.getDescription());
            newCourse.setObject(3, courseDto.getDate_start());
            newCourse.setObject(4, courseDto.getDate_finish());

            int affectedRows = newCourse.executeUpdate();
            int generateId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newCourse.getGeneratedKeys()){
                    if (generatedKeys.next()) {
                        generateId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Course creation failed, no rows added");
            }

            Course newCourseReturn = new Course(generateId, courseDto.getName(), courseDto.getDescription(), courseDto.getDate_start(), courseDto.getDate_finish());

            return newCourseReturn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getAllCourse() {
        String getAll = "SELECT * FROM course ORDER BY name ASC";

        try {
            PreparedStatement getAllCourse = connection.prepareStatement(getAll);

            ResultSet rs = getAllCourse.executeQuery();

            List<Course> courseList = new ArrayList<>();
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
            return null;
        }
    }
}
