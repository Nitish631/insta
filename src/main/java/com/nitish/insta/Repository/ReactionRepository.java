package com.nitish.insta.Repository;

import com.nitish.insta.Entities.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction,Long> {
    @Query("SELECT r FROM Reaction r WHERE r.post.postId=:postId AND r.reactionType=:type")
    Page<Reaction>findByPostIdAndType(@Param("postId") Long postId,@Param("reactionType") String reactionType, Pageable pageable);
    @Query("SELECT r FROM Reaction r WHERE r.post.postId=:postId AND r.user.userId=:userId")
    Optional<Reaction>findByPostAndUser(@Param("postId")Long postId,@Param("userId")Long userId);
    @Query("""
            SELECT r FROM Reaction r
            WHERE r.post.postId=:postId AND r.user.userId IN(
            SELECT f.following.userId FROM Follow f WHERE f.follower.userId=:viewerId)
            """)
    List<Reaction> findFriendsReactionOnPost(@Param("postId")Long postId, @Param("viewerId")Long viewerId);
//     @Query("""
//             SELECT new map(
//             r.user.userId AS userId,
//             r.user.userName AS userName,
//             r.user.profileImage.fileUrl AS profileUrl,
//             r.reactionType AS reactionType
//             ) FROM Reaction r
//             WHERE r.post.postId=:postId AND
//             r.user.userId IN(
//             SELECT f.following.userId FROM Follow f WHERE f.follower.userId=:viewerId)
//             """)
//     List<Object> findFriendReactionsWithDetails(@Param("postId") Long postId,@Param("viewerId")Long viewerId);
    @Query("""
            SELECT r.reactionType,COUNT(r.reactionType)
            FROM Reaction r
            WHERE r.post.postId=:postId
            GROUP BY r.reactionType
            """)
    List<Object[]> countReactionsByType(@Param("postId")Long postId);
    void deleteByUser_UserIdAndPost_PostId(Long userId,Long postId);
}