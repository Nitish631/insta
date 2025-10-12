package com.nitish.insta.Payloads;

import lombok.Data;

@Data
public class OtpRequestDto {
    private String email;
    private String otpToken;
    private String otp;
}
