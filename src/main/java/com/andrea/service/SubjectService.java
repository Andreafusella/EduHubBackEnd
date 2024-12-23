package com.andrea.service;

import com.andrea.dao.SubjectDao;
import com.andrea.dto.AllSubjectDto;
import com.andrea.exception.NotTeacherException;
import com.andrea.model.Subject;

import java.util.List;

public class SubjectService {
    private SubjectDao subjectDao = new SubjectDao();

    public Subject addSubject(Subject subject) throws NotTeacherException {
        return subjectDao.addSubject(subject);
    }

    public List<AllSubjectDto> getAllSubjects() {
        return subjectDao.getAllSubjects();
    }

    public boolean deleteSubject(int id_subject) {
        return subjectDao.deleteSubject(id_subject);
    }
}
