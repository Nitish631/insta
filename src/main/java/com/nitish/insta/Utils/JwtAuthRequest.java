package com.nitish.insta.Utils;

import com.nitish.insta.Validator.ValidateEmail;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtAuthRequest {
    @ValidateEmail
    private String username;
    private String password;
    private String deviceId;
    private String deviceName;
    private String os;
    private String notificationToken;
}
