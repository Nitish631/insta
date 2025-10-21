package com.nitish.insta.Payloads;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDto {
    private Long followerId;
    private Long followingId;
    private String followerUserName;
    private String followingUserName;
    private String followedAt;
}