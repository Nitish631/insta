package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Post;
import com.nitish.insta.Entities.Reaction;
import com.nitish.insta.Entities.ReactionType;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Payloads.ReactionDto;
import com.nitish.insta.Repository.PostRepository;
import com.nitish.insta.Repository.ReactionRepository;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PostRepository postRepo;

    @Override
    public ReactionDto reactToPost(Long postId, Long userId, String reactionTypeStr) {
        ReactionType reactionType = ReactionType.valueOf(reactionTypeStr.toUpperCase());

        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Reaction> existing = reactionRepository.findByPostAndUser(postId, userId);
        Reaction reaction = existing.orElseGet(Reaction::new);

        reaction.setPost(post);
        reaction.setUser(user);
        reaction.setReactionType(reactionType);

        Reaction saved = reactionRepository.save(reaction);

        String profileUrl = user.getProfileImage() != null ? user.getProfileImage().getFileUrl() : null;
        return ReactionDto.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .profileUrl(profileUrl)
                .reactionType(saved.getReactionType())
                .build();
    }

    @Override
    public void removeReaction(Long postId, Long userId) {
        Optional<Reaction> reaction = reactionRepository.findByPostAndUser(postId, userId);
        reaction.ifPresent(reactionRepository::delete);
    }

    @Override
    public Map<String, Long> getReactionCountByPost(Long postId) {
        List<Object[]> result = reactionRepository.countReactionsByType(postId);
        Map<String, Long> counts = new HashMap<>();
        for (Object[] obj : result) {
            ReactionType type = (ReactionType) obj[0];
            Long count = (Long) obj[1];
            counts.put(type.name(), count);
        }
        return counts;
    }

    @Override
    public Page<ReactionDto> getReactionsOfPostByType(Long postId, String reactionType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reaction> reactions = reactionRepository.findByPostIdAndType(postId, reactionType, pageable);
        List<ReactionDto> dtos = reactions.getContent().stream().map(r -> {
            Users u = r.getUser();
            String profileUrl = u.getProfileImage() != null ? u.getProfileImage().getFileUrl() : null;
            return ReactionDto.builder()
                    .userId(u.getUserId())
                    .username(u.getUserName())
                    .profileUrl(profileUrl)
                    .reactionType(r.getReactionType())
                    .build();
        }).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, reactions.getTotalElements());
    }

    @Override
    public List<ReactionDto> getFriendReactionOnPost(Long postId, Long viewerId) {
        List<Reaction> reactions = reactionRepository.findFriendsReactionOnPost(postId, viewerId);
        return reactions.stream().map(r -> {
            Users u = r.getUser();
            String profileUrl = u.getProfileImage() != null ? u.getProfileImage().getFileUrl() : null;
            return ReactionDto.builder()
                    .userId(u.getUserId())
                    .username(u.getUserName())
                    .profileUrl(profileUrl)
                    .reactionType(r.getReactionType())
                    .build();
        }).collect(Collectors.toList());
    }

    // @Override
    // public List<ReactionDto> getFriendsReactionnWithDetails(Long postId, Long viewerId) {
    //     List<Object> result = reactionRepository.findFriendReactionsWithDetails(postId, viewerId);
    //     List<ReactionDto> dtos = new ArrayList<>();

    //     for (Object obj : result) {
    //         if (obj instanceof Map<?, ?> map) {
    //             Long userId = (Long) map.get("userId");
    //             String username = (String) map.get("userName");
    //             String profileUrl = (String) map.get("profileUrl");
    //             ReactionType reactionType = (ReactionType) map.get("reactionType");

    //             dtos.add(ReactionDto.builder()
    //                     .userId(userId)
    //                     .username(username)
    //                     .profileUrl(profileUrl)
    //                     .reactionType(reactionType)
    //                     .build());
    //         }
    //     }
    //     return dtos;
    // }

    @Override
    public ReactionDto getReactionOfUser(Long postId, Long userId) {
        Optional<Reaction> reactionOpt = reactionRepository.findByPostAndUser(postId, userId);
        if (reactionOpt.isEmpty()) return null;

        Reaction r = reactionOpt.get();
        Users u = r.getUser();
        String profileUrl = u.getProfileImage() != null ? u.getProfileImage().getFileUrl() : null;
        return ReactionDto.builder()
                .userId(u.getUserId())
                .username(u.getUserName())
                .profileUrl(profileUrl)
                .reactionType(r.getReactionType())
                .build();
    }

}
