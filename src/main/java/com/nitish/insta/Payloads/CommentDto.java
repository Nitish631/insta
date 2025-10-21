package com.nitish.insta.Payloads;

import java.util.List;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String username;
    private List<CommentDto> replies;
}
