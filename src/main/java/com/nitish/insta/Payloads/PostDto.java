package com.nitish.insta.Payloads;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Integer postId;
    private String title;
    private String contentDescription;
    private boolean postPrivate;
    private boolean allowComments;
    private Instant createdAt;
    private Instant updatedAt;
    private UserSummaryDto user;
    private List<CommentDto> comments;
}
