package com.nitish.insta.Payloads;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Integer id;
    private String name;
}
