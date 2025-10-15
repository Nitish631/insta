package com.nitish.insta.Payloads;

import com.nitish.insta.Validator.ValidateEmail;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequestDto {
    private String otpToken;
    @ValidateEmail
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?]).*$",message = "The password must contain uppercase ,lowercase,number,and a special character" )
    @Size(min=6,max = 20,message = "Password must be between 6 characters to 20 characters")
    private String password;
}
