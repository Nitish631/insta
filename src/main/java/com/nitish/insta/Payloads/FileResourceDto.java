package com.nitish.insta.Payloads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResourceDto {
    private long id;
    private String fileName;
    private String fileUrl;
    private String publicId;
    private String fileType;
    private String description;
    private String title;
    private boolean accessibled;
}
