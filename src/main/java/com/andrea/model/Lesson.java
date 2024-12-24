package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Lesson {
    private Integer id_lesson;

    @NonNull
    private Integer id_course;

    @NonNull
    private Integer id_subject;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lesson_date;

    @NonNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime hour_start;

    @NonNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime hour_end;

    @NonNull
    private String classroom;

}
