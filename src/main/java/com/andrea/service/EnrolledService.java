package com.andrea.service;

import com.andrea.dao.EnrolledDao;
import com.andrea.dto.RemoveEnrolledDto;
import com.andrea.exception.AccountNotFoundException;
import com.andrea.exception.CourseNotFoundException;
import com.andrea.exception.EnrolledExistException;
import com.andrea.exception.EnrolledNotExistException;
import com.andrea.model.Course;
import com.andrea.model.Enrolled;

import java.util.List;

public class EnrolledService {
    private EnrolledDao enrolledDao = new EnrolledDao();

    public Enrolled addEnrolled(Enrolled enrolled) throws EnrolledExistException, AccountNotFoundException, CourseNotFoundException {
        return enrolledDao.addEnrolled(enrolled);
    }

    public int removeEnrolled(RemoveEnrolledDto enrolled) throws EnrolledNotExistException {
        return enrolledDao.removeEnrolled(enrolled);
    }

    public List<Course> getAllEnrolledCourse(int id_account, boolean inCourse) {
        return enrolledDao.getAllEnrolledCourse(id_account, inCourse);
    }
}
