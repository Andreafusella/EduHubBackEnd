package com.andrea.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Subject {
    private Integer id_subject;

    @NonNull
    private String name;

    @NonNull
    private Integer id_course;

    @NonNull
    private Integer id_teacher;

    public Subject(String name, Integer id_course, Integer id_teacher) {
        this.name = name;
        this.id_course = id_course;
        this.id_teacher = id_teacher;
    }

}
