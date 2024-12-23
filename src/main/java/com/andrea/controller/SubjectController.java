package com.andrea.controller;

import com.andrea.exception.NotTeacherException;
import com.andrea.model.Subject;
import com.andrea.service.SubjectService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SubjectController {
    private SubjectService subjectService = new SubjectService();

    public void registerRoutes(Javalin app) {
        app.post("/subject", this::addSubject);
        app.get("/subjects", this::getAllSubjects);
        app.delete("/subject", this::deleteSubject);
    }

    public void addSubject(Context ctx) {
        try {
            System.out.println("New Subject");

            Subject subject = null;
            try {
                subject = ctx.bodyAsClass(Subject.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            //TODO aggiungere validazione campi body

            Subject newSubject = subjectService.addSubject(subject);

            if (newSubject == null) {
                ctx.status(400).json("Invalid request");
            } else {
                ctx.status(201).json(newSubject);
            }
        } catch (Exception e) {
            if (e instanceof NotTeacherException) {
                ctx.status(400).json(e.getMessage());
            } else {
                ctx.status(400).json("Error: " + e.getMessage());
            }
        }
    }

    public void getAllSubjects(Context ctx) {
        try {
            System.out.println("Get Subjects");

            List<Subject> subjects = subjectService.getAllSubjects();

            if (subjects.isEmpty()) {
                ctx.status(404).json("No subjects found");
            } else {
                ctx.status(200).json(subjects);
            }
        } catch (Exception e) {
            ctx.status(500).json("Error fetching subjects: " + e.getMessage());
        }
    }

    public void deleteSubject(Context ctx) {
        try {
            System.out.println("Delete Subject");

            int id_subject = 0;
            try {
                id_subject = Integer.parseInt(ctx.queryParam("id_subject"));
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            boolean isDeleted = subjectService.deleteSubject(id_subject);

            if (isDeleted) {
                ctx.status(200).json("Subject deleted successfully");
            } else {
                ctx.status(404).json("Subject not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid subject ID");
        } catch (Exception e) {
            ctx.status(500).json("Error deleting subject: " + e.getMessage());
        }
    }

}
