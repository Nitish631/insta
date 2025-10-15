package com.nitish.insta.Payloads;

import com.nitish.insta.Validator.ValidateEmail;
import lombok.Data;

@Data
public class OtpRequestDto {
    @ValidateEmail
    private String email;
    private String otpToken;
    private String otp;
}
