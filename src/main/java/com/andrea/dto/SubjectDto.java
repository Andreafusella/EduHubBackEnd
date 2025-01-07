package com.andrea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectDto {
    @NonNull
    private Integer id_subject;

    @NonNull
    private String subject_name;
}
