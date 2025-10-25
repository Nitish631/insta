package com.nitish.insta.Payloads;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryDto {
    private Long userId;
    private String userName;
    private String fullName;
    private String profileUrl;
    private Instant createdAt;
}
