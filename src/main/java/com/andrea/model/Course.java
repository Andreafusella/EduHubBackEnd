package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Course {
    private Integer id_course;

    @NonNull
    private String name;

    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_start;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_finish;

    public Course(String name, String description, LocalDate date_start, LocalDate date_finish) {
        this.name = name;
        this.description = description;
        this.date_start = date_start;
        this.date_finish = date_finish;

    }
}
