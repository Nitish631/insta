package com.nitish.insta.Payloads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryDto {
    private Long userId;
    private String username;
    private String fullName;
}
