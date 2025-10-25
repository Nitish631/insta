package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.*;
import com.nitish.insta.Payloads.PostResponseDto;
import com.nitish.insta.Payloads.PostRequestDto;
import com.nitish.insta.Repository.*;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.FileService;
import com.nitish.insta.Service.PostService;

import com.nitish.insta.Service.ReactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsersRepo usersRepository;

    @Autowired
    private MainCategoryRepo mainCategoryRepository;

    @Autowired
    private SubCategoryRepo subCategoryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenHelper tokenHelper;
    @Autowired
    private ReactionService reactionService;
    @Autowired
    private BadSubCategoryRepo badSubCategoryRepo;
    @Autowired
    private BadMainCategoryRepo badMainCategoryRepo;

    // ================= CREATE POST =================
    @Override
    public PostResponseDto createPost(PostRequestDto postRequest, String jwtToken) throws Exception {
        String email = tokenHelper.getUserNameFromToken(jwtToken);
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContentDescription(postRequest.getContentDescription());
        post.setPostPrivate(postRequest.isPostPrivate());
        post.setAllowComments(postRequest.isAllowComments());
        post.setReels(postRequest.isReel());
        post.setUser(user);
        post.setCreatedAt(Instant.now());

        if (postRequest.getMainCategoryIds() != null) {
            post.setMainCategories(postRequest.getMainCategoryIds().stream()
                    .map(mainCategoryRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toSet()));
        }

        if (postRequest.getSubCategoryIds() != null) {
            post.setSubCategories(postRequest.getSubCategoryIds().stream()
                    .map(subCategoryRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toSet()));
        }

        // Handle file uploads (private for posts)
        if (postRequest.getFiles() != null) {
            for (MultipartFile file : postRequest.getFiles()) {
                FileResource resource=fileService.uploadFile(file, "Post File", post.getTitle()); // uploads to private folder
                post.getFiles().add(resource);
            }
        }

        return mapToDto(postRepository.save(post));
    }

    // ================= UPDATE POST =================
    @Override
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequest, String jwtToken) throws Exception {
        String email = tokenHelper.getUserNameFromToken(jwtToken);
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized: cannot update this post");
        }

        if (postRequest.getTitle() != null) post.setTitle(postRequest.getTitle());
        if (postRequest.getContentDescription() != null) post.setContentDescription(postRequest.getContentDescription());
        post.setAllowComments(postRequest.isAllowComments());
        post.setPostPrivate(postRequest.isPostPrivate());
        post.setUpdatedAt(Instant.now());

        if (postRequest.getMainCategoryIds() != null) {
            post.setMainCategories(postRequest.getMainCategoryIds().stream()
                    .map(mainCategoryRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toSet()));
        }

        if (postRequest.getSubCategoryIds() != null) {
            post.setSubCategories(postRequest.getSubCategoryIds().stream()
                    .map(subCategoryRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toSet()));
        }

        // Handle file uploads (optional)
        if (postRequest.getFiles() != null) {
            for (MultipartFile file : postRequest.getFiles()) {
                FileResource resource=fileService.uploadFile(file, "Updated Post File", post.getTitle());
                post.getFiles().add(resource);
            }
        }

        return mapToDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        return postRepository.findById(postId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public void deletePost(Long postId,String jwtToken) throws Exception{
        String email=tokenHelper.getUserNameFromToken(jwtToken);
        Post post=this.postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
        Users tokenUser=this.usersRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        if(!post.getUser().getUserId().equals(tokenUser.getUserId())) {
            Set<FileResource> files=post.getFiles();
            for(FileResource file:files){
                fileService.deleteFile(file.getId());
            }
            postRepository.deleteById(postId);
        }else {
            throw new RuntimeException("Unauthorized: cannot delete this post");
        }
    }

    @Override
    public Page<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByReelsFalse(pageable).map(this::mapToDto);
    }

    @Override
    public Page<PostResponseDto> getPostsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUser_UserId(userId, pageable).map(this::mapToDto);
    }

    @Override
    public Page<PostResponseDto> getPostsByMainCategory(Integer mainCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByMainCategories_Id(mainCategoryId, pageable).map(this::mapToDto);
    }

    @Override
    public Page<PostResponseDto> getPostsBySubCategory(Integer subCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findBySubCategories_Id(subCategoryId, pageable).map(this::mapToDto);
    }
    public String reportPost(Long postId,String description,Long reporterUserId,Integer badMainCategoryId,Integer badSubCategoryId){
        Post post=this.postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
        Report report=new Report();
        Users reporterUser=this.usersRepository.findById(reporterUserId).orElseThrow(()->new RuntimeException("User not found"));
        report.setReportedBy(reporterUser);
        report.setPost(post);
        if(description!=null && !description.isEmpty()){
        report.setDescription(description);
        }
        if(badMainCategoryId!=null){
            BadMainCategories badMainCategory=this.badMainCategoryRepo.findById(badMainCategoryId).orElseThrow(()->new RuntimeException("Main Category not found"));
            report.setBadMainCategories(badMainCategory);
        }
        if(badSubCategoryId!=null){
            BadSubCategories badSubCategory=this.badSubCategoryRepo.findById(badSubCategoryId).orElseThrow(()->new RuntimeException("Sub Category not found"));
            report.setBadSubCategories(badSubCategory);
        }
        report.setCreatedAt(Instant.now());
        post.getReports().add(report);
        this.postRepository.save(post);
        return "Post reported successfully";
    }

    @Override
    public Page<PostResponseDto> findReportedPosts(int page, int size, Integer badMainCategoryId, Integer badSubCategoryId) {
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdAt").descending());
        if(badMainCategoryId!=null && badSubCategoryId!=null){
            Page<Post> posts=this.postRepository.findReportedPosts(badMainCategoryId,pageable);
            return posts.map(this::mapToDto);
        }
        return null;
    }

    // ================= DTO Mapper =================
    private PostResponseDto mapToDto(Post post) {
        PostResponseDto dto = modelMapper.map(post, PostResponseDto.class);
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getUserId());
            dto.setUsername(post.getUser().getUserName());
            dto.setUserProfileUrl(post.getUser().getProfileImage() != null
                    ? post.getUser().getProfileImage().getFileUrl() : null);
        }

        dto.setMainCategoryIds(post.getMainCategories().stream()
                .map(MainCategory::getId).collect(Collectors.toSet()));
        dto.setSubCategoryIds(post.getSubCategories().stream()
                .map(SubCategory::getId).collect(Collectors.toSet()));

        dto.setCommentCount(post.getComments().size());
        if(post.getFiles()!=null){
            post.getFiles().forEach(file->{
                String singedUrl=fileService.generatedSignedUrl(file.getId(),120);
                dto.getFileUrls().add(singedUrl);
            });
        }
        Map<String,Long> reactionCounts=reactionService.getReactionCountByPost(post.getPostId());
        if(reactionCounts!=null){
            dto.setReactionCount(reactionCounts);
        }
        return dto;
    }
}