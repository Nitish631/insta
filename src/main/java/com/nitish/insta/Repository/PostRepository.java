package com.nitish.insta.Repository;

import com.nitish.insta.Entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser_UserId(Long userId, Pageable pageable);

    Page<Post> findByReelsTrueAndPostPrivateFalse(Pageable pageable);

    Page<Post> findByReelsFalse(Pageable pageable);

    Page<Post> findByMainCategories_Id(Integer mainCategoryId, Pageable pageable);

    Page<Post> findBySubCategories_Id(Integer subCategoryId, Pageable pageable);
    @Query("""
            SELECT DISTINCT p FROM Post p
            JOIN p.reports r
            WHERE
            (:badCategoryId IS NULL OR r.badMainCategories.id=:badCategoryId)
            """)
    Page<Post>findReportedPosts(@Param("badCategoryId")Integer badCategoryId,
                                Pageable pageable);

}
