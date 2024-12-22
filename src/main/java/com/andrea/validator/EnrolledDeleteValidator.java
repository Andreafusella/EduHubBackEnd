package com.andrea.validator;

import com.andrea.dto.RemoveEnrolled;
import com.andrea.exception.ValidationException;
import com.andrea.model.Enrolled;

public class EnrolledDeleteValidator {

    public static void validate(RemoveEnrolled enrolled) throws ValidationException {

        if (enrolled.getId_account() <= 0) {
            throw new ValidationException("Invalid 'id_account'. It must be a positive integer.");
        }

        if (enrolled.getId_course() <= 0) {
            throw new ValidationException("Invalid 'id_course'. It must be a positive integer.");
        }

    }
}
