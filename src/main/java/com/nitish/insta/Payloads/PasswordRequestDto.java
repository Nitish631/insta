package com.nitish.insta.Payloads;

import lombok.Data;

@Data
public class PasswordRequestDto {
    private String otpToken;
    private String email;
    private String password;
}
