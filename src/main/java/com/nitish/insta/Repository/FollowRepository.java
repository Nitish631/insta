package com.nitish.insta.Repository;

import com.nitish.insta.Entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower_UserId(Long userId);
    List<Follow> findByFollowing_UserId(Long userId);
}
