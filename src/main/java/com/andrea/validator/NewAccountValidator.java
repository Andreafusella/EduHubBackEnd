package com.andrea.validator;

import com.andrea.dto.NewAccountDto;
import com.andrea.exception.ValidationException;

public class NewAccountValidator {

    public static void validate(NewAccountDto account) throws ValidationException {

        if (account.getName() == null || account.getName().isEmpty()) {
            throw new ValidationException("Name cannot be null or empty");
        }

        // Cognome: non deve essere nullo
        if (account.getLast_name() == null || account.getLast_name().isEmpty()) {
            throw new ValidationException("Last name cannot be null or empty");
        }

        // Ruolo: deve essere Student, Teacher, Administrator
        if (account.getRole() == null ||
                (!account.getRole().equals("Student") &&
                        !account.getRole().equals("Teacher") &&
                        !account.getRole().equals("Administrator"))) {
            throw new ValidationException("Role must be 'Student', 'Teacher', or 'Administrator'");
        }

        // Email: deve essere valida
        if (account.getEmail() == null ||
                !account.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new ValidationException("Invalid email format");
        }

        // Password: min 5 caratteri, una lettera maiuscola
        if (account.getPassword() == null ||
                account.getPassword().length() < 5 ||
                !account.getPassword().matches(".*[A-Z].*")) {
            throw new ValidationException("Password must be at least 5 characters long and contain at least one uppercase letter");
        }
    }
}
