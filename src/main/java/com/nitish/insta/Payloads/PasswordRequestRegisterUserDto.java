package com.nitish.insta.Payloads;

import com.nitish.insta.Validator.ValidateEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequestRegisterUserDto {
    private String otpToken;
    @ValidateEmail
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?]).*$",message = "Password must contain uppercase ,lowercase,number,and a special character" )
    @Size(min=6,max = 20,message = "Password must be between 6 characters to 20 characters")
    private String password;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String fullName;
    private String deviceName;
    private String deviceId;
    private String os;
    private String notificationToken;
}
