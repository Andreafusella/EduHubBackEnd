package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Enrolled {
    @NonNull
    private Integer id_account;

    @NonNull
    private Integer id_course;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollment_date;
}
