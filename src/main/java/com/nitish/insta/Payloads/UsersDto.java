package com.nitish.insta.Payloads;

import com.nitish.insta.Validator.ValidateEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private int userId;
    @NotEmpty(message = "Name field is empty")
    private String userName;
    @ValidateEmail(message = "invalid email")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?]).*$",message = "The password must contain uppercase ,lowercase,number,and a special character" )
    @Size(min=6,max = 20,message = "Password must be between 6 characters to 20 characters")
    private String password;
    private String fullName;
}
