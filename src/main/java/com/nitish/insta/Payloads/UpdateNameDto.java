package com.nitish.insta.Payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateNameDto {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;
}
