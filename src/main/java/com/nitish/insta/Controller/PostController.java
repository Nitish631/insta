package com.nitish.insta.Controller;

import com.nitish.insta.Payloads.PostResponseDto;
import com.nitish.insta.Payloads.PostRequestDto;
import com.nitish.insta.Service.PostService;
import com.nitish.insta.Security.JwtTokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtTokenHelper jwtTokenHelper;

    // ✅ Create a normal post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestHeader("Authorization") String token,
            @ModelAttribute PostRequestDto postRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        token=token.substring(7);
        try{
        PostResponseDto created = postService.createPost(postRequest, token);
        return ResponseEntity.ok(created);
        } catch (Exception e) {
            throw new RuntimeException("Error creating post");
        }
    }

    // ✅ Update existing post
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token,
            @ModelAttribute PostRequestDto postRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        token=token.substring(7);
        try{
        PostResponseDto updated = postService.updatePost(postId, postRequest,token);
        return ResponseEntity.ok(updated);
        } catch (Exception e) {
            throw new RuntimeException("Error updating post");
        }
    }

    // ✅ Delete post
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) throws IOException {
        token=token.substring(7);
        try{
        postService.deletePost(postId, token);
        return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting post");
        }
    }

    // ✅ Get all public posts (non-reel)
    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }
}
