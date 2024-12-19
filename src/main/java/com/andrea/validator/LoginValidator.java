package com.andrea.validator;

import com.andrea.auth.dto.LoginDto;
import com.andrea.exception.ValidationException;

public class LoginValidator {

    public static void validate(LoginDto login) throws ValidationException {
        if (login.getEmail() == null ||
                !login.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new ValidationException("Invalid email format");
        }

        // Password: min 5 caratteri, una lettera maiuscola
        if (login.getPassword() == null ||
                login.getPassword().length() < 5 ||
                !login.getPassword().matches(".*[A-Z].*")) {
            throw new ValidationException("Password must be at least 5 characters long and contain at least one uppercase letter");
        }
    }
}
