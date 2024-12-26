package com.andrea.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Question {
    private Integer id_question;

    @NonNull
    private String question;

    @NonNull
    private String textA;

    @NonNull
    private String textB;

    @NonNull
    private String textC;

    @NonNull
    private String textD;

    @NonNull
    private String right_answer;

    @NonNull
    private Integer id_quiz;

}
