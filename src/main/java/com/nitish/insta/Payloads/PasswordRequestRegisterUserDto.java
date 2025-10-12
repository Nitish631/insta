package com.nitish.insta.Payloads;

import lombok.Data;

@Data
public class PasswordRequestRegisterUserDto {
    private String otpToken;
    private String email;
    private String password;
    private String fullName;
}
