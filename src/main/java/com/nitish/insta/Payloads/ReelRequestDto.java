package com.nitish.insta.Payloads;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReelRequestDto {
    private String title; // optional caption
    private String contentDescription; // optional
    private boolean postPrivate;  // required
    private boolean allowComments; // required

    private MultipartFile videoFile; // required
    private MultipartFile audioFile; // optional
}
