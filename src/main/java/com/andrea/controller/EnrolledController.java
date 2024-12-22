package com.andrea.controller;

import com.andrea.exception.AccountNotFoundException;
import com.andrea.exception.CourseNotFoundException;
import com.andrea.exception.EnrolledExistException;
import com.andrea.exception.ValidationException;
import com.andrea.model.Enrolled;
import com.andrea.service.EnrolledService;
import com.andrea.validator.EnrolledValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class EnrolledController {
    private EnrolledService enrolledService = new EnrolledService();

    public void registerRoutes(Javalin app) {
        app.post("/enrolled", this::addEnrolled);
    }

    public void addEnrolled(Context ctx){
        try {
            System.out.println("New Enrolled");

            Enrolled enrolled = null;
            try {
                enrolled = ctx.bodyAsClass(Enrolled.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            EnrolledValidator.validate(enrolled);

            enrolledService.addEnrolled(enrolled);

            ctx.status(201).json("Enrolled Created!");
        } catch (ValidationException e) {
            ctx.status(400).json(e.getMessage());
        } catch (EnrolledExistException e) {
            ctx.status(400).json(e.getMessage());
        } catch (AccountNotFoundException e) {
            ctx.status(400).json(e.getMessage());
        } catch (CourseNotFoundException e) {
            ctx.status(400).json(e.getMessage());
        }
    }
}
