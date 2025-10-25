package com.nitish.insta.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nitish.insta.Entities.VideoAudio;
import com.nitish.insta.Repository.VideoAudioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class MediaService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private VideoAudioRepo videoAudioRepo;


    public VideoAudio uploadMedia(MultipartFile videoFile, MultipartFile audioFile) throws IOException {
        VideoAudio media = new VideoAudio();

        // Upload video as PUBLIC
        if (videoFile != null) {
            Map videoUpload = cloudinary.uploader().upload(videoFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", "public_media",
                            "access_mode", "public" // PUBLIC access
                    ));
            media.setVideoUrl(videoUpload.get("secure_url").toString());
            media.setVideoPublicId(videoUpload.get("public_id").toString());
            media.setVideoFormat(videoFile.getContentType());
        }

        // Upload audio as PUBLIC
        if (audioFile != null) {
            Map audioUpload = cloudinary.uploader().upload(audioFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "folder", "public_media",
                            "access_mode", "public" // PUBLIC access
                    ));
            media.setAudioUrl(audioUpload.get("secure_url").toString());
            media.setAudioPublicId(audioUpload.get("public_id").toString());
            media.setAudioFormat(audioFile.getContentType());
        }

        return videoAudioRepo.save(media);
    }

    public void deleteMedia(VideoAudio media) throws IOException {
        if (media.getVideoPublicId() != null) {
            cloudinary.uploader().destroy(media.getVideoPublicId(), ObjectUtils.asMap("resource_type", "video"));
        }
        if (media.getAudioPublicId() != null) {
            cloudinary.uploader().destroy(media.getAudioPublicId(), ObjectUtils.asMap("resource_type", "raw"));
        }
        videoAudioRepo.delete(media);
    }
}
