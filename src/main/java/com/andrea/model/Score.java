package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Score {
    private Integer id_score;

    @NonNull
    private Integer id_quiz;

    @NonNull
    private Integer id_account;

    @NonNull
    private int score;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
