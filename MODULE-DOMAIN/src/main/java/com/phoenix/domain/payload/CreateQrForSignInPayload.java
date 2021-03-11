package com.phoenix.domain.payload;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateQrForSignInPayload {
    private String token;
    private String appName;
}
