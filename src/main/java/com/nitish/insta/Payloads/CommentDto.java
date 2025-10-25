package com.nitish.insta.Payloads;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class CommentDto {
    private Integer commentId;
    private String username;
    private String content;
    private Integer postId;
    private Long userId;
    private Integer parentCommentId;
    private List<CommentDto> replies;
    private Instant createdAt;
    private boolean hidedComment;
}
