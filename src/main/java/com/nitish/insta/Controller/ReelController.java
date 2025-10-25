package com.nitish.insta.Controller;

import com.nitish.insta.Payloads.ReelRequestDto;
import com.nitish.insta.Payloads.ReelResponseDto;
import com.nitish.insta.Service.ReelService;
import com.nitish.insta.Security.JwtTokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
public class ReelController {

    private final ReelService reelService;
    private final JwtTokenHelper jwtTokenHelper;

    // Create a reel
    @PostMapping
    public ResponseEntity<ReelResponseDto> createReel(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ReelRequestDto reelRequest) throws IOException {
        token = token.substring(7);
        try{
        return ResponseEntity.ok(reelService.createReel(reelRequest, token));
        } catch (Exception e) {
            throw new RuntimeException("Error creating reel.");
        }
    }

    // Get reel by id
    @GetMapping("/{id}")
    public ResponseEntity<ReelResponseDto> getReelById(@PathVariable Long id) {
        return ResponseEntity.ok(reelService.getReelById(id));
    }

    // Delete reel
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReel(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) throws IOException {
        token = token.substring(7);
        try{
        reelService.deleteReel(id, token);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting reel.");
        }
        return ResponseEntity.ok("Reel deleted successfully");
    }

    // Get all reels
    @GetMapping
    public ResponseEntity<Page<ReelResponseDto>> getAllReels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reelService.getAllReels(page, size));
    }
    @PutMapping("/{postId}")
    public ResponseEntity<ReelResponseDto> updateReel(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token,
            @ModelAttribute ReelRequestDto reelRequest
    ) throws Exception {
        token = token.substring(7);
        ReelResponseDto updated = reelService.updateReel(postId, reelRequest, token);
        return ResponseEntity.ok(updated);
    }
}
