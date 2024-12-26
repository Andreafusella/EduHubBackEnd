package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Quiz {
    private Integer id_quiz;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate quiz_date;

    @NonNull
    private Integer id_subject;
}
