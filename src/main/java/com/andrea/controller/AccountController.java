package com.andrea.controller;

import com.andrea.dto.NewAccountDto;
import com.andrea.exception.EmailExistException;
import com.andrea.exception.ValidationException;
import com.andrea.model.Account;
import com.andrea.model.AccountWithEmail;
import com.andrea.service.AccountService;
import com.andrea.utility.email.EmailService;
import com.andrea.validator.NewAccountValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class AccountController {
    private AccountService accountService = new AccountService();

    public void registerRoutes(Javalin app) {
        app.post("/register", this::register);
        app.get("/get-account-with-email", this::getAccountWithEmail);
    }

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

            Account newAccount = accountService.addAccount(account);

            if (newAccount == null) {
                ctx.status(400).json("Invalid request");
            } else {
                try {
                    EmailService emailService = new EmailService("andrearisparmi15@gmail.com", "qkum fufq gaxi jvky");
                    emailService.sendEmail(
                            account.getEmail(),
                            "Welcome to Our Service",
                            "Your account is now ready for start!"
                    );
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

    public void getAccountWithEmail(Context ctx) {
        System.out.println("Get account with Email");
        List<AccountWithEmail> listAccount= accountService.getAllAccountWithEmail();

        if (listAccount == null) {
            ctx.status(204).json("There aren't Account");
        } else {
            ctx.status(201).json(listAccount);
        }
    }
}
