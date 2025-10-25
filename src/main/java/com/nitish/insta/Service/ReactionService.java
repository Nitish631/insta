package com.nitish.insta.Service;

import com.nitish.insta.Payloads.ReactionDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReactionService {
    ReactionDto reactToPost(Long postId, Long userId, String reactionType);
    void removeReaction(Long postId,Long userId);
    Map<String,Long> getReactionCountByPost(Long postId);
//    List<ReactionDto> getReactionsOfPost(Long postId);
    ReactionDto getReactionOfUser(Long postId,Long userId);
    Page<ReactionDto> getReactionsOfPostByType(Long postId,String reactionType,int page,int size);
    List<ReactionDto>getFriendReactionOnPost(Long postId,Long viewerId);
    // List<ReactionDto> getFriendsReactionnWithDetails(Long postId, Long viewerId);
}
