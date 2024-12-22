package com.andrea.validator;

import com.andrea.exception.ValidationException;
import com.andrea.model.Enrolled;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EnrolledValidator {

    public static void validate(Enrolled enrolled) throws ValidationException {

        if (enrolled.getId_account() <= 0) {
            throw new ValidationException("Invalid 'id_account'. It must be a positive integer.");
        }


        if (enrolled.getId_course() <= 0) {
            throw new ValidationException("Invalid 'id_course'. It must be a positive integer.");
        }


        if (enrolled.getEnrollment_date() == null) {
            throw new ValidationException("Invalid 'enrollment_date'. It must not be null.");
        }

        if (!isValidEnrollmentDate(enrolled.getEnrollment_date())) {
            throw new ValidationException("Invalid 'enrollment_date'. It must be in the format 'yyyy-MM-dd'.");
        }
    }

    // Metodo per validare il formato della data
    private static boolean isValidEnrollmentDate(LocalDate enrollmentDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {

            String formattedDate = enrollmentDate.format(formatter);

            return true;
        } catch (DateTimeParseException e) {

            return false;
        }
    }
}