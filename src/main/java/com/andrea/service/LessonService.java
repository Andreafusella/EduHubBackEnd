package com.andrea.service;

import com.andrea.dao.LessonDao;
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
}