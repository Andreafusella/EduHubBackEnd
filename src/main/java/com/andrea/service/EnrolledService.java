package com.andrea.service;

import com.andrea.dao.EnrolledDao;
import com.andrea.exception.AccountNotFoundException;
import com.andrea.exception.CourseNotFoundException;
import com.andrea.exception.EnrolledExistException;
import com.andrea.model.Enrolled;

public class EnrolledService {
    private EnrolledDao enrolledDao = new EnrolledDao();

    public void addEnrolled(Enrolled enrolled) throws EnrolledExistException, AccountNotFoundException, CourseNotFoundException {
        enrolledDao.addEnrolled(enrolled);
    }
}
