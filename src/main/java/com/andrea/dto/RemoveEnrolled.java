package com.andrea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class RemoveEnrolled {

    @NonNull
    private Integer id_account;

    @NonNull
    private Integer id_course;
}
