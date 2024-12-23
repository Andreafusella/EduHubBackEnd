package com.andrea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class NewCourseDto {
    @NonNull
    private String name;

    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_start;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_finish;

}
