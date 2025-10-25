package com.nitish.insta.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private Users reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bad_main_category_id")
    private BadMainCategories badMainCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bad_sub_category_id")
    private BadSubCategories badSubCategories;

    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
