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
    private LocalDate dateStart;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFinish;

    @NonNull
    private int id_teacher;

    public Course(String name, String description, LocalDate dateStart, LocalDate dateFinish, int id_teacher) {
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.id_teacher = id_teacher;
    }
}
