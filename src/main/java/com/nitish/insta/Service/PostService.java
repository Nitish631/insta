package com.nitish.insta.Service;

import com.nitish.insta.Payloads.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Integer postId);
    List<PostDto> getAllPosts();
    PostDto updatePost(Integer postId, PostDto postDto);
    void deletePost(Integer postId);
}
