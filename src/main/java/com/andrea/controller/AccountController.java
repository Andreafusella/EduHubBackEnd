package com.andrea.controller;

import com.andrea.dto.NewAccountDto;
import com.andrea.dto.PresenceDto;
import com.andrea.exception.EmailExistException;
import com.andrea.exception.ValidationException;
import com.andrea.model.AccountWithEmail;
import com.andrea.service.AccountService;
import com.andrea.utility.email.EmailService;
import com.andrea.validator.NewAccountValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class AccountController {
    private AccountService accountService = new AccountService();
    private EmailService emailService = new EmailService();

    public void registerRoutes(Javalin app) {
        app.post("/register", this::register);
        app.get("/get-account-with-email", this::getAccountWithEmail);
        app.get("/get-account-with-email-with-role", this::getAllAccountWithEmailWithParam);
        app.delete("/delete", this::deleteAccount);
        app.get("/student", this::getStudent);
        app.get("/get-account-by-course", this::getAllStudentByCourse);
        app.get("/get-studentNotInCourse-by-course", this::getAllStudentsNotInCourse);
        app.get("/get-student-by-email", this::getStudentByEmail);
        app.get("/get-student-by-presence", this::getStudentByPresence);
    }


    //registrazione utente
    public void register(Context ctx) {
        try {
            System.out.println("New Account");

            NewAccountDto account = null;
            try {
                account = ctx.bodyAsClass(NewAccountDto.class);
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            NewAccountValidator.validate(account);

            AccountWithEmail newAccount = accountService.addAccount(account);

            if (newAccount == null) {
                ctx.status(400).json("Invalid request");
            } else {
                try {
                    emailService.generateAndSendEmail(account.getEmail(), "Create Account");
                    ctx.status(201).json(newAccount);
                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                }
            }

        } catch (EmailExistException e) {
            ctx.status(409).json("Email already Exist!");
        } catch (ValidationException e) {
            ctx.status(400).json(e.getMessage());
        }
    }

    //delete account
    public void deleteAccount(Context ctx) {
        try {
            System.out.println("Delete account");

            int id_account = 0;

            try {
                id_account = Integer.parseInt(ctx.queryParam("id_account"));
            } catch (Exception e) {
                ctx.status(400).json("Invalid input data: " + e.getMessage());
            }

            String email = accountService.removeAccount(id_account);

            if (email != null) {
                try {
                    emailService.generateAndSendEmail(email, "Delete Account");
                    ctx.status(201).json("Account delete");
                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //get al account
    public void getAccountWithEmail(Context ctx) {
        System.out.println("Get account with Email");
        List<AccountWithEmail> listAccount= accountService.getAllAccountWithEmail();

        if (listAccount == null) {
            ctx.status(404).json("There aren't Account");
        } else {
            ctx.status(201).json(listAccount);
        }
    }

    //get all account with one param
    public void getAllAccountWithEmailWithParam(Context ctx) {
        System.out.println("Get account with Email with Role");

        String role = ctx.queryParam("role");

        if (role == null || role.isEmpty()) {
            role = "Default";
        }
        List<AccountWithEmail> listAccount = accountService.getAllAccountWithEmailWithParam(role);

        if (listAccount == null || listAccount.isEmpty()) {
            ctx.status(404).json("There aren't any accounts for the role: " + role);
        } else {
            ctx.status(201).json(listAccount);
        }
    }

    public void getStudent(Context ctx) {
        System.out.println("Get student");

        String idAccountParam = ctx.queryParam("id_account");

        if (idAccountParam == null || idAccountParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_account' parameter.");
        }

        System.out.println("Received id_account parameter: " + idAccountParam);
        try {

            Integer id_account = Integer.parseInt(idAccountParam.trim());

            AccountWithEmail account = accountService.getStudent(id_account);

            if (account == null) {
                ctx.status(404).json("No account found with id: " + id_account);
            } else {
                ctx.status(200).json(account);
            }
        } catch (NumberFormatException e) {

            ctx.status(400).json("Invalid 'id_account' parameter. It must be an integer.");
        }
    }

    public void getAllStudentByCourse(Context ctx) {
        System.out.println("Get all students by course");

        String idCourseParam = ctx.queryParam("id_course");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }

        System.out.println("Received id_course parameter: " + idCourseParam);
        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());

            List<AccountWithEmail> students = accountService.getStudentsByCourse(id_course);


            if (students == null || students.isEmpty()) {
                ctx.status(404).json("No students found for course id: " + id_course);
            } else {

                ctx.status(200).json(students);
            }
        } catch (NumberFormatException e) {

            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }

    public void getAllStudentsNotInCourse(Context ctx) {
        System.out.println("Get all students not in course");

        String idCourseParam = ctx.queryParam("id_course");

        if (idCourseParam == null || idCourseParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_course' parameter.");
            return;
        }


        try {
            Integer id_course = Integer.parseInt(idCourseParam.trim());

            List<AccountWithEmail> studentsNotInCourse = accountService.getStudentsNotInCourse(id_course);

            if (studentsNotInCourse == null || studentsNotInCourse.isEmpty()) {
                ctx.status(404).json("No students found who are not enrolled in course id: " + id_course);
            } else {
                ctx.status(200).json(studentsNotInCourse);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_course' parameter. It must be an integer.");
        }
    }

    public void getStudentByEmail(Context ctx) {
        System.out.println("Get student by email");

        String emailParam = ctx.queryParam("email");

        if (emailParam == null || emailParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'email' parameter.");
            return;
        }

        try {
            AccountWithEmail student = accountService.getStudentByEmail(emailParam);

            if (student == null) {
                ctx.status(404).json("No student found with email: " + emailParam);
            } else {
                ctx.status(200).json(student);
            }
        } catch (Exception e) {
            ctx.status(500).json("Error while fetching student: " + e.getMessage());
        }
    }

    public void getStudentByPresence(Context ctx) {
        System.out.println("Get student by Presence");

        String lessonParam = ctx.queryParam("id_lesson");

        if (lessonParam == null || lessonParam.isEmpty()) {
            ctx.status(400).json("Missing or invalid 'id_lesson' parameter.");
            return;
        }

        try {
            Integer id_lesson = Integer.parseInt(lessonParam.trim());

            List<PresenceDto> listAccount = accountService.getStudentByPresence(id_lesson);

            if (listAccount == null || listAccount.isEmpty()) {
                ctx.status(204).json("No Account found for id_lesson id: " + id_lesson);
            } else {
                ctx.status(200).json(listAccount);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid 'id_lesson' parameter. It must be an integer.");
        }
    }

}
