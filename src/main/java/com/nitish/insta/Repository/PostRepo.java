package com.nitish.insta.Repository;

import com.nitish.insta.Entities.Post;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    Page<Post> findDistinctByMainCategories_Id(Integer mainCategoryId, SpringDataWebProperties.Pageable pageable);
    Page<Post> findDistinctBySubCategories_Id(Integer subCategoryId, Pageable pageable);
    Page<Post> findDistinctByBadMainCategories_Id(Integer badMainCategoryId, Pageable pageable);
    Page<Post> findDistinctByBadSubCategories_Id(Integer badSubCategoryId, Pageable pageable);
    Page<Post> findByUser_UserId(Long userId, Pageable pageable);
}
