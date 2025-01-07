package com.andrea.service;

import com.andrea.dao.SubjectDao;
import com.andrea.dto.AllSubjectDto;
import com.andrea.dto.SubjectDto;
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

    public List<AllSubjectDto> getAllSubjectByIdTeacher(int id_teacher) {
        return subjectDao.getAllSubjectByIdTeacher(id_teacher);
    }

    public Subject getSubjectById(int id_subject) {
        return subjectDao.getSubjectById(id_subject);
    }

    public List<AllSubjectDto> getSubjectByCourse(int id_course) {
        return subjectDao.getSubjectByCourse(id_course);
    }

    public List<SubjectDto> getSubjectByAccount(int id_account) {
        return subjectDao.getSubjectByAccount(id_account);
    }
}
