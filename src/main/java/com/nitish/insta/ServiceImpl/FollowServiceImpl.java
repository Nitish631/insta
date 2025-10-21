package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Follow;
import com.nitish.insta.Payloads.FollowDto;
import com.nitish.insta.Repository.FollowRepository;
import com.nitish.insta.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowRepository followRepo;
    @Override
    public List<FollowDto> getFollowersOfUser(Long userId) {
        List<Follow> followers = followRepo.findByFollowing_UserId(userId);
        return followers.stream().map(f -> new FollowDto(
                f.getFollower().getUserId(),
                f.getFollowing().getUserId(),
                f.getFollower().getUserName(),
                f.getFollowing().getUserName(),
                f.getFollowedAt().toString()
        )).toList();
    }

    @Override
    public List<FollowDto> getFollowingOfUser(Long userId) {
        List<Follow> following = followRepo.findByFollower_UserId(userId);
        return following.stream().map(f -> new FollowDto(
                f.getFollower().getUserId(),
                f.getFollowing().getUserId(),
                f.getFollower().getUserName(),
                f.getFollowing().getUserName(),
                f.getFollowedAt().toString()
        )).toList();
    }

}
