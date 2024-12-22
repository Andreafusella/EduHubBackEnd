package com.andrea.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithEmail {

    @NonNull
    private int id_account;

    @NonNull
    private String name;

    @NonNull
    private String lastName;

    @NonNull
    private String role;

    @NonNull
    private String email;

    @NonNull
    private int avatar;
}
