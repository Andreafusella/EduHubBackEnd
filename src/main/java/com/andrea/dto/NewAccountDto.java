package com.andrea.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NewAccountDto {

    @NonNull
    private String name;

    @NonNull
    private String last_name;

    @NonNull
    private String role;

    @NonNull
    private String email;

    @NonNull
    private String password;
}
