package com.andrea.controller;

import com.andrea.dto.NewCourseDto;
import com.andrea.model.Course;
import com.andrea.service.CourseSerivce;
import com.andrea.utility.email.EmailService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CourseController {
    private CourseSerivce courseSerivce = new CourseSerivce();
    private EmailService emailService = new EmailService();

    public void registerRoutes(Javalin app) {
        app.post("/add-course", this::addCourse);
    }

    //new course
    public void addCourse(Context ctx) {
        try {
            System.out.println("New Course");

            NewCourseDto course = null;
            try {
                course = ctx.bodyAsClass(NewCourseDto.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            //todo aggiungere validazione campi body

            Course newCourse = courseSerivce.addCourse(course);

            if (newCourse == null) {
                ctx.status(400).json("Invalid request");
            } else {
                ctx.status(201).json(newCourse);
            }

        } catch (Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    }

}
