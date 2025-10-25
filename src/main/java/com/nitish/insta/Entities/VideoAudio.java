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
public class VideoAudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String videoUrl;
    private String videoPublicId;
    private String videoFormat;

    private String audioUrl;
    private String audioPublicId;
    private String audioFormat;

    @CreationTimestamp
    private Instant createdAt;

}
