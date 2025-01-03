package com.andrea.service;

import com.andrea.dao.LessonDao;
import com.andrea.dto.LessonListPresenceStudentDto;
import com.andrea.exception.NotSubjectException;
import com.andrea.model.Lesson;

import java.util.List;

public class LessonService {
    private LessonDao lessonDao = new LessonDao();

    public Lesson addLesson(Lesson lessons) throws NotSubjectException {
        return lessonDao.addLesson(lessons);
    }

    public boolean deleteLesson(int id_lesson) {
        return lessonDao.deleteLesson(id_lesson);
    }

    public List<Lesson> getLessonsByCourseId(int id_course) {
        return lessonDao.getLessonsByCourseId(id_course);
    }

    public List<Lesson> getLastLessons(int id_course, boolean flag) {
        return lessonDao.getPrevLessons(id_course, flag);
    }

    public List<Lesson> get5LessonsBySubject(int id_course, boolean flag) {
        return lessonDao.get5LessonsBySubject(id_course, flag);
    }

    public List<Lesson> getLessonsBySubjectId(int id_subject) {
        return lessonDao.getLessonsBySubjectId(id_subject);
    }

    public List<LessonListPresenceStudentDto> get5LastLessonByAccount(int id_account, boolean limit) {
        return lessonDao.get5LastLessonByAccount(id_account, limit);
    }
}
