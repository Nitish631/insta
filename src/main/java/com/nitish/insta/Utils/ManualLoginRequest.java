package com.nitish.insta.Utils;

import lombok.Data;

@Data
public class ManualLoginRequest {
    private String username;
    private String password;
    private String token;
}
