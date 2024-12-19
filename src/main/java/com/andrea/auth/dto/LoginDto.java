package com.andrea.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class LoginDto {

    @NonNull
    private String email;

    @NonNull
    private String password;
}
