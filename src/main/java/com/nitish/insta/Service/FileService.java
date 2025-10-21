package com.nitish.insta.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nitish.insta.Entities.FileResource;
import com.nitish.insta.Repository.FileResourceRepo;

@Service
public class FileService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private FileResourceRepo fileResourceRepo;

    public FileResource updateFile(Long id, MultipartFile file, String description, String title)
            throws IOException {
        FileResource fileResource = this.fileResourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found."));
        String resourceType = file.getContentType().startsWith("image") ? "image"
                : file.getContentType().startsWith("video") ? "video" : "raw";

        cloudinary.uploader().destroy(fileResource.getPublicId(),
                ObjectUtils.asMap("resource_type", fileResource.getFileType()));
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", resourceType,
                        "folder", "private_files" ,
                        "access_mode", "authenticated" ));
        fileResource.setFileName(file.getOriginalFilename());
        fileResource.setFileType(resourceType);
        fileResource.setFileUrl(uploadResult.get("secure_url").toString());
        fileResource.setPublicId(uploadResult.get("public_id").toString());
        fileResource.setDescription(description);
        fileResource.setTitle(title);
        return fileResourceRepo.save(fileResource);
    }
    void changeAccessibility(Long id,boolean accessible){
        FileResource resource=this.fileResourceRepo.findById(id).orElseThrow(()->new RuntimeException("File not found."));
        resource.setAccessibled(accessible);
        this.fileResourceRepo.save(resource);
    }

    public FileResource uploadFile( MultipartFile file, String description, String title)
            throws IOException {
        FileResource fileResource = new FileResource();
        String resourceType = file.getContentType().startsWith("image") ? "image"
                : file.getContentType().startsWith("video") ? "video" : "raw";

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", resourceType,
                        "folder", "private_files" ,
                        "access_mode", "authenticated" ));
        fileResource.setFileName(file.getOriginalFilename());
        fileResource.setFileType(resourceType);
        fileResource.setFileUrl(uploadResult.get("secure_url").toString());
        fileResource.setPublicId(uploadResult.get("public_id").toString());
        fileResource.setDescription(description);
        fileResource.setTitle(title);
        return fileResourceRepo.save(fileResource);
    }

    public String deleteFile(Long id) throws IOException {
        FileResource fileResource = fileResourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        cloudinary.uploader().destroy(fileResource.getPublicId(), ObjectUtils.asMap("resource_type", "auto"));
        fileResourceRepo.delete(fileResource);
        return "Deleted successfully";
    }

    // public String getFile(Long id) throws IOException {
    //     FileResource fileResource = this.fileResourceRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("File not found"));
    //     return fileResource.getFileUrl();
    // }

    public String generatedSignedUrl(Long id, int expireminute) {
        FileResource fileResource = this.fileResourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        long expireAt = (System.currentTimeMillis() / 1000L) + (expireminute * 60L);
        return cloudinary.url()
                .secure(true)
                .signed(true)
                .transformation(new com.cloudinary.Transformation().chain().param("timestamp", expireAt))
                .resourceType(fileResource.getFileType())
                .generate(fileResource.getPublicId());
    }
}
