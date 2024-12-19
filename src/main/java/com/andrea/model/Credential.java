package com.andrea.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Credential {
    private Integer id_account;

    @NonNull
    private String email;

    @NonNull
    private String password;
}
