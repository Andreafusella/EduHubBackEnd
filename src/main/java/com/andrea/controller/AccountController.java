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
import java.util.Map;

public class AccountController {
    private AccountService accountService = new AccountService();
    private EmailService emailService = new EmailService();

    public void registerRoutes(Javalin app) {
        app.post("/register", this::register);
        app.get("/get-account-with-email", this::getAccountWithEmail);
        app.get("/get-account-with-email-with-role", this::getAllAccountWithEmailWithParam);
        app.delete("/delete", this::deleteAccount);
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

            Account newAccount = accountService.addAccount(account);

            if (newAccount == null) {
                ctx.status(400).json("Invalid request");
            } else {
                try {
                    emailService.generateAndSendEmail(account.getEmail(), "Welcome to Our Service", "Your account is now ready for start!", "try");
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
                    emailService.generateAndSendEmail(email, "Elimination Complete", "Your account was successfully deleted!", "try");
                    ctx.status(201).json("Account delete");
                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getAccountWithEmail(Context ctx) {
        System.out.println("Get account with Email");
        List<AccountWithEmail> listAccount= accountService.getAllAccountWithEmail();

        if (listAccount == null) {
            ctx.status(404).json("There aren't Account");
        } else {
            ctx.status(201).json(listAccount);
        }
    }

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
}
