package com.andrea.controller;

import com.andrea.dto.RemoveEnrolledDto;
import com.andrea.exception.*;
import com.andrea.model.Course;
import com.andrea.model.Enrolled;
import com.andrea.service.EnrolledService;
import com.andrea.validator.EnrolledDeleteValidator;
import com.andrea.validator.EnrolledValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class EnrolledController {
    private EnrolledService enrolledService = new EnrolledService();

    public void registerRoutes(Javalin app) {
        app.post("/enrolled", this::addEnrolled);
        app.delete("/enrolled", this::removeEnrolled);
        app.get("/enrolled", this::getAllEnrolledCourse);
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

            Enrolled enrolledReturn = enrolledService.addEnrolled(enrolled);

            ctx.status(201).json(enrolledReturn);
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

    public void removeEnrolled(Context ctx)  {
        try {
            System.out.println("Delete Enrolled");

            RemoveEnrolledDto removeEnrolled = null;

            try {
                removeEnrolled = ctx.bodyAsClass(RemoveEnrolledDto.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            System.out.println(removeEnrolled.getId_account());

            EnrolledDeleteValidator.validate(removeEnrolled);

            int rowsAffected = enrolledService.removeEnrolled(removeEnrolled);

            if (rowsAffected == 1) {
                //TODO: inviare email rimozione
                ctx.status(201).json("Enrolled delete");
            } else if (rowsAffected == -1) {
                ctx.status(404).json("No enrolled record found to delete.");
            } else {
                ctx.status(500).json("Unexpected error occurred while deleting enrolled.");
            }
        } catch (ValidationException e) {
            ctx.status(400).json(e.getMessage());
        } catch (EnrolledNotExistException e) {
            ctx.status(400).json(e.getMessage());
        }
    }

    public void getAllEnrolledCourse(Context ctx) {
        try {
            System.out.println("Get Enrolled Course");

            String idAccountParam = ctx.queryParam("id_account");
            String flagParam = ctx.queryParam("in_course");


            if (flagParam == null || flagParam.isEmpty()) {
                ctx.status(400).json("Missing 'id_account' parameter.");
                return;
            }

            if (!flagParam.equalsIgnoreCase("true") && !flagParam.equalsIgnoreCase("false")) {
                ctx.status(400).json("Invalid 'in_course' parameter. It must be 'true' or 'false'.");
                return;
            }

            int id_account = 0;
            try {
                id_account = Integer.parseInt(idAccountParam);
                boolean in_course = Boolean.parseBoolean(flagParam.trim());

                List<Course> courseList = enrolledService.getAllEnrolledCourse(id_account, in_course);

                if (courseList != null && !courseList.isEmpty()) {
                    ctx.status(200).json(courseList);
                } else {
                    ctx.status(404).json("Not found any enrolled on course");
                }

            } catch (NumberFormatException e) {
                ctx.status(400).json("Invalid 'id_account' parameter: it must be an integer.");
                return;
            }


        } catch (Exception e) {
            System.out.println(e);
            ctx.status(500).json("An error occurred while processing the request.");
        }
    }
}
