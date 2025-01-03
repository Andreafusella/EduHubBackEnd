package com.andrea.controller;

import com.andrea.dto.LessonListPresenceStudentDto;
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
        app.get("/prev-lesson", this::getPrevLessons);
        app.get("/prev-lesson-by-subjectId", this::get5LessonsBySubject);
        app.get("/lesson-by-subjectId", this::getLessonsBySubjectId);
        app.get("/lesson-by-account", this::get5LastLessonByAccount);

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

    public void getLessonsBySubjectId(Context ctx) {
        System.out.println("Get Lesson By Subject Id");

        String idSubjectParam = ctx.queryParam("id_subject");

        if (idSubjectParam == null || idSubjectParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_subject' parameter.");
            return;
        }

        try {
            Integer id_subject = Integer.parseInt(idSubjectParam.trim());

            List<Lesson> lessons = lessonService.getLessonsBySubjectId(id_subject);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(204).json("No Lessons found for subject id: " + id_subject);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_subject' parameter. It must be an integer.");
        }
    }

    public void getPrevLessons(Context ctx) {
        System.out.println("Get Lessons Next/Last");

        String idCourseParam = ctx.queryParam("id_course");
        String flagParam = ctx.queryParam("next");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }

        if (!flagParam.equalsIgnoreCase("true") && !flagParam.equalsIgnoreCase("false")) {
            ctx.status(400).json("Invalid 'next' parameter. It must be 'true' or 'false'.");
            return;
        }


        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());
            boolean flag = Boolean.parseBoolean(flagParam.trim());

            List<Lesson> lessons = lessonService.getLastLessons(id_course, flag);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(204).json("No recent lessons found for course id: " + id_course);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }

    public void get5LessonsBySubject(Context ctx) {
        System.out.println("Get Lessons Next/Last By Subject");

        String idSubjectParam = ctx.queryParam("id_subject");
        String flagParam = ctx.queryParam("next");

        if (idSubjectParam == null || idSubjectParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_subject' parameter.");
            return;
        }

        if (!flagParam.equalsIgnoreCase("true") && !flagParam.equalsIgnoreCase("false")) {
            ctx.status(400).json("Invalid 'next' parameter. It must be 'true' or 'false'.");
            return;
        }


        try {
            Integer id_subject = Integer.parseInt(idSubjectParam.trim());
            boolean flag = Boolean.parseBoolean(flagParam.trim());

            List<Lesson> lessons = lessonService.get5LessonsBySubject(id_subject, flag);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(204).json("No recent lessons found for subject id: " + id_subject);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_subject' parameter. It must be an integer.");
        }
    }

    public void get5LastLessonByAccount(Context ctx) {
        System.out.println("Get Lessons Last By Account");

        String idAccountParam = ctx.queryParam("id_account");
        String limitParam = ctx.queryParam("limit");

        if (idAccountParam == null || idAccountParam.isEmpty() || limitParam == null || limitParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid parameter.");
            return;
        }

        try {
            Integer id_account = Integer.parseInt(idAccountParam.trim());
            boolean limit = Boolean.parseBoolean(limitParam.trim());

            List<LessonListPresenceStudentDto> lessons = lessonService.get5LastLessonByAccount(id_account, limit);

            if (lessons == null || lessons.isEmpty()) {
                ctx.status(204).json("No lessons found for id_account id: " + id_account);
            } else {
                ctx.status(200).json(lessons);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_account' parameter. It must be an integer.");
        }
    }

}
