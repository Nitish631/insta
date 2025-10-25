package com.nitish.insta.Service;
import com.nitish.insta.Payloads.PostResponseDto;
import com.nitish.insta.Payloads.PostRequestDto;
import org.springframework.data.domain.Page;

public interface PostService {
    PostResponseDto createPost(PostRequestDto postRequest, String jwtToken) throws Exception;
    PostResponseDto updatePost(Long postId, PostRequestDto postRequest, String jwtToken) throws Exception;
    PostResponseDto getPostById(Long postId);
    void deletePost(Long postId,String jwtToken)throws Exception;
    Page<PostResponseDto> getAllPosts(int page, int size);
    Page<PostResponseDto> getPostsByUserId(Long userId, int page, int size);
    Page<PostResponseDto> getPostsByMainCategory(Integer mainCategoryId, int page, int size);
    Page<PostResponseDto> getPostsBySubCategory(Integer subCategoryId, int page, int size);
    Page<PostResponseDto>findReportedPosts(int page, int size,Integer badMainCategoryId,Integer badSubCategoryId);
}
