package com.nitish.insta.Payloads;

import com.nitish.insta.Validator.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {
    @ValidateEmail
    private String email;
    private boolean forReset;
}
