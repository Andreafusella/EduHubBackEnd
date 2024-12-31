package com.andrea.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Presence {

    @NonNull
    private Integer id_account;

    @NonNull
    private Integer id_lesson;

    @NonNull
    private Boolean presence;


}
