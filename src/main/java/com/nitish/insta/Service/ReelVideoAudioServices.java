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
public class ReelVideoAudioServices {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private VideoAudioRepo reelVideoAudioRepo;

    public VideoAudio uploadReelFiles(MultipartFile videoFile, MultipartFile audioFile) throws IOException {

        Map videoUpload = cloudinary.uploader().upload(videoFile.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", "public_reels/videos",
                        "access_mode", "public"
                ));

        Map audioUpload = cloudinary.uploader().upload(audioFile.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", "public_reels/audios",
                        "access_mode", "public"
                ));

        VideoAudio reelFile = new VideoAudio();
        reelFile.setVideoUrl(videoUpload.get("secure_url").toString());
        reelFile.setVideoPublicId(videoUpload.get("public_id").toString());
        reelFile.setVideoFormat(videoUpload.get("format").toString());

        reelFile.setAudioUrl(audioUpload.get("secure_url").toString());
        reelFile.setAudioPublicId(audioUpload.get("public_id").toString());
        reelFile.setAudioFormat(audioUpload.get("format").toString());

        return reelVideoAudioRepo.save(reelFile);
    }

    public String deleteReelFiles(Long id) throws IOException {
        VideoAudio reelFile = reelVideoAudioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reel not found"));

        // delete both video + audio from cloudinary
        cloudinary.uploader().destroy(reelFile.getVideoPublicId(),
                ObjectUtils.asMap("resource_type", "video"));
        cloudinary.uploader().destroy(reelFile.getAudioPublicId(),
                ObjectUtils.asMap("resource_type", "video"));

        reelVideoAudioRepo.delete(reelFile);
        return "Reel deleted successfully";
    }

    public VideoAudio updateReel(Long id, MultipartFile newVideo, MultipartFile newAudio) throws IOException {

        VideoAudio existing = reelVideoAudioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reel not found"));

        if (newVideo != null && !newVideo.isEmpty()) {
            cloudinary.uploader().destroy(existing.getVideoPublicId(),
                    ObjectUtils.asMap("resource_type", "video"));

            Map videoUpload = cloudinary.uploader().upload(newVideo.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", "public_reels/videos",
                            "access_mode", "public"
                    ));

            existing.setVideoUrl(videoUpload.get("secure_url").toString());
            existing.setVideoPublicId(videoUpload.get("public_id").toString());
            existing.setVideoFormat(videoUpload.get("format").toString());
        }

        // If new audio provided, delete old and upload new
        if (newAudio != null && !newAudio.isEmpty()) {
            cloudinary.uploader().destroy(existing.getAudioPublicId(),
                    ObjectUtils.asMap("resource_type", "video"));

            Map audioUpload = cloudinary.uploader().upload(newAudio.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", "public_reels/audios",
                            "access_mode", "public"
                    ));

            existing.setAudioUrl(audioUpload.get("secure_url").toString());
            existing.setAudioPublicId(audioUpload.get("public_id").toString());
            existing.setAudioFormat(audioUpload.get("format").toString());
        }

        return reelVideoAudioRepo.save(existing);
    }

}
