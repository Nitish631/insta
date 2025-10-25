package com.nitish.insta.Payloads;

import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;//
    private String title;//
    private String contentDescription;//
    private boolean postPrivate;//
    private boolean allowComments;//
    private boolean isReel;//
    private Long userId;//
    private String username;
    private String userProfileUrl;
    private Instant createdAt;//
    private Instant updatedAt;//
    private Set<String>fileUrls;//

    private Set<Integer> mainCategoryIds;//
    private Set<Integer> subCategoryIds;//

    private int commentCount;
    private Map<String,Long> reactionCount;
}
