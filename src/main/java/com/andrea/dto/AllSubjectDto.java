package com.andrea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllSubjectDto {
    @NonNull
    private Integer id_subject;

    @NonNull
    private String name;

    @NonNull
    private Integer id_course;

    private String name_course;

    @NonNull
    private Integer id_teacher;

    private String name_teacher;
}
