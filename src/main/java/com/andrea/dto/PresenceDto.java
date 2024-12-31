package com.andrea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PresenceDto {

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

    @NonNull
    private boolean presence;
}
