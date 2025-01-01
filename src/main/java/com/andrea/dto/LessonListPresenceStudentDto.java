package com.andrea.dto;

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

public class LessonListPresenceStudentDto {

    @NonNull
    private Integer id_lesson;

    @NonNull
    private String title;

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
    private boolean presence;
}
