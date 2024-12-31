package com.andrea.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class File {

    private Integer id_file;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String path;

    @NonNull
    private Integer id_subject;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateUpload;
}
