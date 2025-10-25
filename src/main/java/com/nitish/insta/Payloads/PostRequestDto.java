package com.nitish.insta.Payloads;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto {

    private String title;                     // nullable
    private String contentDescription;        // nullable
    private boolean postPrivate;              // required
    private boolean allowComments;            // required
    private boolean isReel;                   // required

    private Set<Integer> mainCategoryIds;     // nullable
    private Set<Integer> subCategoryIds;      // nullable

    private MultipartFile[] files;            // nullable
}