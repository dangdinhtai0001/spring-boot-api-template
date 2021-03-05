package com.phoenix.domain.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignInByQrCodePayload {
    private String code;
    private String username;
}
