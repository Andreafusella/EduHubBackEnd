package com.andrea.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer id_account;

    @NonNull
    private String name;

    @NonNull
    private String last_name;

    @NonNull
    private String role;

    private Account(String name, String last_name, String role) {
        this.name = name;
        this.last_name = last_name;
        this.role = role;
    }
}
