package com.nitish.insta.Entities;

import java.time.Instant;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer postId;
    @Column(name = "title",length = 100,nullable = false)
    private String title;
    @Column(length = 10000)
    private String contentDescription;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<FileResource> videosAndImages=new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "mainCategory_post",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="mainCategory_id"))
    private Set<MainCategory> mainCategories;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "subCategory_post",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="subCategory_id"))
    private Set<SubCategory> subCategories;
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="badMainCategory_post",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="badMainCategory_id"))
    private List<BadMainCategories> badMainCategoriesList=new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="badSubCategory_post",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="badSubCategory_id"))
    private List<BadSubCategories> badSubCategoriesList=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    private Users user;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments=new HashSet<>();
    private boolean postPrivate=false;
    private boolean allowComments=true;
}
