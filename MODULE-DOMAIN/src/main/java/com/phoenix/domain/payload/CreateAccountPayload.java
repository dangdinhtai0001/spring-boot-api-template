package com.phoenix.domain.payload;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountPayload {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}
