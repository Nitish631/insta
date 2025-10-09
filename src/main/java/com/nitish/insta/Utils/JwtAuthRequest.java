package com.nitish.insta.Utils;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtAuthRequest {
    private String username;
    private String password;
}
