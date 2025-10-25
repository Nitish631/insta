package com.nitish.insta.Payloads;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoAudioDto {
    private Long id;
    private String videoUrl;
    private String videoPublicId;
    private String videoFormat;
    private String audioUrl;
    private String audioPublicId;
    private String audioFormat;
    private Instant createdAt;
}
