package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Post;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Payloads.PostDto;
import com.nitish.insta.Payloads.UserSummaryDto;
import com.nitish.insta.Repository.PostRepo;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepository;
    @Autowired
    private UsersRepo usersRepository;

    @Override
    public PostDto createPost(PostDto postDto) {
        Users user = usersRepository.findById(postDto.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContentDescription(postDto.getContentDescription());
        post.setPostPrivate(postDto.isPostPrivate());
        post.setAllowComments(postDto.isAllowComments());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToDto(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(Integer postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postDto.getTitle());
        post.setContentDescription(postDto.getContentDescription());
        post.setPostPrivate(postDto.isPostPrivate());
        post.setAllowComments(postDto.isAllowComments());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post) {
        UserSummaryDto userDto = new UserSummaryDto(
                post.getUser().getUserId(),
                post.getUser().getUserName(),
                post.getUser().getFullName()
        );

        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .contentDescription(post.getContentDescription())
                .postPrivate(post.isPostPrivate())
                .allowComments(post.isAllowComments())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .user(userDto)
                .build();
    }
}
