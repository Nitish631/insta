package com.nitish.insta.Service;

import com.nitish.insta.Payloads.FollowDto;

import java.util.List;

public interface FollowService {
    List<FollowDto> getFollowersOfUser(Long userId);
    List<FollowDto> getFollowingOfUser(Long userId);

}
