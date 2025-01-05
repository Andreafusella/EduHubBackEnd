package com.andrea.controller;

import com.andrea.auth.middleware.JwtAuthMiddleware;
import com.andrea.dto.NewCourseDto;
import com.andrea.model.Course;
import com.andrea.service.CourseSerivce;
import com.andrea.utility.email.EmailService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CourseController {
    private JwtAuthMiddleware jwtAuthMiddleware = new JwtAuthMiddleware();
    private CourseSerivce courseSerivce = new CourseSerivce();
    private EmailService emailService = new EmailService();

    public void registerRoutes(Javalin app) {

        app.post("/add-course", this::addCourse);

        app.before("/courses", jwtAuthMiddleware::handle);
        app.get("/courses", this::getAllCourse);
        app.get("/course-by-id", this::getCourseById);
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

            //TODO aggiungere validazione campi body

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

    public void getAllCourse(Context ctx) {
        try {
            System.out.println("Get Course");

            List<Course> courseList = courseSerivce.getAllCourse();

            if (courseList == null || courseList.isEmpty()) {
                ctx.status(400).json("There'isnt course");
            } else {
                ctx.status(201).json(courseList);
            }
        } catch (Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    }

    public void getCourseById(Context ctx) {
        try {
            System.out.println("Get Course By Id");
            int id_course = 0;
            try {
                id_course = Integer.parseInt(ctx.queryParam("id_course"));
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            Course course = courseSerivce.getCourseById(id_course);

            if (course == null) {
                ctx.status(404).json("Course not found");
            } else {
                ctx.status(200).json(course);
            }

        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid course ID format");
        } catch (Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    }

}
