package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Post;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Entities.VideoAudio;
import com.nitish.insta.Payloads.ReelRequestDto;
import com.nitish.insta.Payloads.ReelResponseDto;
import com.nitish.insta.Repository.PostRepository;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.MediaService;
import com.nitish.insta.Service.ReelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReelServiceImpl implements ReelService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JwtTokenHelper tokenHelper;

    @Autowired
    private MediaService mediaService;

    @Override
    public ReelResponseDto createReel(ReelRequestDto reelRequest, String jwtToken) throws Exception {
        String email = tokenHelper.getUserNameFromToken(jwtToken);
        Users user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post reelPost = new Post();
        // Null-safe title and description
        if (reelRequest.getTitle() != null) reelPost.setTitle(reelRequest.getTitle());
        if (reelRequest.getContentDescription() != null)
            reelPost.setContentDescription(reelRequest.getContentDescription());

        reelPost.setReels(true);
        reelPost.setPostPrivate(reelRequest.isPostPrivate());
        reelPost.setAllowComments(reelRequest.isAllowComments());
        reelPost.setUser(user);
        reelPost.setCreatedAt(Instant.now());

        // Upload video/audio if provided
        if (reelRequest.getVideoFile() != null || reelRequest.getAudioFile() != null) {
            VideoAudio media = mediaService.uploadMedia(reelRequest.getVideoFile(), reelRequest.getAudioFile());
            if (media != null) reelPost.getVideoAudios().add(media);
        }

        Post savedPost = postRepository.save(reelPost);
        return mapToReelDto(savedPost);
    }

    @Override
    public ReelResponseDto getReelById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        return mapToReelDto(post);
    }

    @Override
    public void deleteReel(Long postId, String jwtToken) throws Exception {
        String email = tokenHelper.getUserNameFromToken(jwtToken);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        Users user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized to delete this reel");
        }

        // Null-safe deletion of media
        if (post.getVideoAudios() != null && !post.getVideoAudios().isEmpty()) {
            VideoAudio media = post.getVideoAudios().iterator().next();
            if (media != null) mediaService.deleteMedia(media);
        }

        postRepository.delete(post);
    }

    @Override
    public ReelResponseDto updateReel(Long postId, ReelRequestDto reelRequest, String jwtToken) throws Exception {
        String email = tokenHelper.getUserNameFromToken(jwtToken);
        Users user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post reelPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));

        if (!reelPost.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized to update this reel");
        }

        // Null-safe update of title and description
        if (reelRequest.getTitle() != null) reelPost.setTitle(reelRequest.getTitle());
        if (reelRequest.getContentDescription() != null)
            reelPost.setContentDescription(reelRequest.getContentDescription());

        reelPost.setPostPrivate(reelRequest.isPostPrivate());
        reelPost.setAllowComments(reelRequest.isAllowComments());
        reelPost.setUpdatedAt(Instant.now());

        // Replace video/audio if new files provided
        if (reelRequest.getVideoFile() != null || reelRequest.getAudioFile() != null) {
            VideoAudio oldMedia = null;
            if (reelPost.getVideoAudios() != null && !reelPost.getVideoAudios().isEmpty()) {
                oldMedia = reelPost.getVideoAudios().iterator().next();
            }

            if (oldMedia != null) {
                mediaService.deleteMedia(oldMedia);
                reelPost.getVideoAudios().clear();
            }

            VideoAudio newMedia = mediaService.uploadMedia(reelRequest.getVideoFile(), reelRequest.getAudioFile());
            if (newMedia != null) reelPost.getVideoAudios().add(newMedia);
        }

        Post savedPost = postRepository.save(reelPost);
        return mapToReelDto(savedPost);
    }

    @Override
    public Page<ReelResponseDto> getAllReels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByReelsTrueAndPostPrivateFalse(pageable);
        return posts.map(this::mapToReelDto);
    }

    private ReelResponseDto mapToReelDto(Post post) {
        ReelResponseDto dto = new ReelResponseDto();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContentDescription(post.getContentDescription());
        dto.setPostPrivate(post.isPostPrivate());
        dto.setAllowComments(post.isAllowComments());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        // Null-safe video/audio mapping
        if (post.getVideoAudios() != null && !post.getVideoAudios().isEmpty()) {
            VideoAudio va = post.getVideoAudios().iterator().next();
            if (va != null) {
                dto.setVideoUrl(va.getVideoUrl());
                dto.setAudioUrl(va.getAudioUrl());
                dto.setVideoFormat(va.getVideoFormat());
                dto.setAudioFormat(va.getAudioFormat());
            }
        }

        // Null-safe user mapping
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getUserId());
            dto.setUsername(post.getUser().getUserName());
            dto.setUserProfileUrl(post.getUser().getProfileImage() != null
                    ? post.getUser().getProfileImage().getFileUrl() : null);
        }

        // Null-safe comment/reaction counts
        dto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
        dto.setReactionCount(post.getReactions() != null ? post.getReactions().size() : 0);

        return dto;
    }
}
