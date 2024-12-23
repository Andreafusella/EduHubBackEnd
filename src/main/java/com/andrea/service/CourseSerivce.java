package com.andrea.service;

import com.andrea.dao.CourseDao;
import com.andrea.dto.NewCourseDto;
import com.andrea.model.Course;

import java.util.List;

public class CourseSerivce {
    private CourseDao courseDao = new CourseDao();

    public Course addCourse(NewCourseDto courseDto) {
        return courseDao.addCourse(courseDto);
    }

    public List<Course> getAllCourse() {
        return courseDao.getAllCourse();
    }

    public Course getCourseById(int id_course) {
        return courseDao.getCourseById(id_course);
    }

}
