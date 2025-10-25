package com.nitish.insta.Payloads;

import com.nitish.insta.Entities.ReactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDto {
    private Long userId;
    private String username;
    private String profileUrl;
    private ReactionType reactionType;
}
