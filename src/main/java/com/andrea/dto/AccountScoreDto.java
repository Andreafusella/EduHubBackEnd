package com.andrea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountScoreDto {

    @NonNull
    private Integer id_account;

    @NonNull
    private String name;

    @NonNull
    private String last_name;

    @NonNull
    private String email;

    @NonNull
    private double score;

    @NonNull
    private Integer avatar;
}
