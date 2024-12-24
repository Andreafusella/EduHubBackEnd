package com.andrea.controller;

import com.andrea.dto.NewCourseDto;
import com.andrea.exception.NotSubjectException;
import com.andrea.model.Lesson;
import com.andrea.service.LessonService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class LessonController {

    private LessonService lessonService = new LessonService();

    public void registerRoutes(Javalin app) {
        app.post("/lesson", this::addLesson);
        app.delete("/lesson", this::deleteLesson);
        app.get("/lesson-by-courseId", this::getLessonsByCourseId);
        app.get("/last-lesson", this::getLastLessons);
        app.get("/next-lesson", this::getNextLessons);
    }

    public void addLesson(Context ctx) {
        try {
            System.out.println("New Lesson");

            Lesson lesson = null;
            try {
                lesson = ctx.bodyAsClass(Lesson.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            //TODO aggiungere validazioni campi body

            Lesson newLesson = lessonService.addLesson(lesson);

            if (newLesson == null) {
                ctx.status(400).json("Invalid request");
            } else {
                ctx.status(201).json(newLesson);
            }
        } catch (NotSubjectException e) {
            ctx.status(400).json(e.getMessage());
        }  catch (Exception e) {
        ctx.status(400).json("Error");
        }
    }

    public void deleteLesson(Context ctx) {
        try {
            System.out.println("Delete Lesson");

            String idLessonParam = ctx.queryParam("id_lesson");

            if (idLessonParam == null || idLessonParam.isEmpty()) {
                ctx.status(400).json("Missing 'id_lesson' parameter.");
                return;
            }

            int id_lesson = 0;
            try {
                id_lesson = Integer.parseInt(idLessonParam);
            } catch (NumberFormatException e) {
                ctx.status(400).json("Invalid 'id_account' parameter: it must be an integer.");
                return;
            }

            boolean delete = lessonService.deleteLesson(id_lesson);

            if (delete) {
                ctx.status(201).json("Lesson delete");
            } else if (!delete) {
                ctx.status(404).json("No lesson record found to delete.");
            } else {
                ctx.status(500).json("Unexpected error occurred while deleting lesson.");
            }
        } catch (Exception e) {
            ctx.status(500).json("Unexpected error occurred while deleting lesson.");
        }
    }

    public void getLessonsByCourseId(Context ctx) {
        System.out.println("Get Lesson By Course Id");

        String idCourseParam = ctx.queryParam("id_course");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }

        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());

            List<Lesson> lessons = lessonService.getLessonsByCourseId(id_course);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(404).json("No Lessons found for course id: " + id_course);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }

    public void getLastLessons(Context ctx) {
        System.out.println("Get Last Lessons");

        String idCourseParam = ctx.queryParam("id_course");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }

        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());

            List<Lesson> lessons = lessonService.getLastLessons(id_course);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(404).json("No recent lessons found for course id: " + id_course);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }

    public void getNextLessons(Context ctx) {
        System.out.println("Get Next Lessons");

        String idCourseParam = ctx.queryParam("id_course");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }

        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());

            List<Lesson> lessons = lessonService.getNextLessons(id_course);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(404).json("No upcoming lessons found for course id: " + id_course);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }
}
