package com.nitish.insta.Payloads;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReelResponseDto {
    private Long postId;
    private String title;
    private String contentDescription;
    private boolean postPrivate;
    private boolean allowComments;

    private String videoUrl;
    private String audioUrl;
    private String videoFormat;
    private String audioFormat;

    private Long userId;
    private String username;
    private String userProfileUrl;

    private Instant createdAt;
    private Instant updatedAt;

    private int commentCount;
    private int reactionCount;
}
