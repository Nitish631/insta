package com.nitish.insta.Entities;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name="comment_id")
    private int commentId;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",referencedColumnName = "postId")
    private Post post;
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "userId")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parentComment;
    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> replies=new ArrayList<>();
    private Instant createdAt=Instant.now();
    @Column(name = "hidedComment",columnDefinition = "TINYINT(1)")
    private boolean hidedComment;
    
}
