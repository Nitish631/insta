package com.nitish.insta.Service;

import com.nitish.insta.Payloads.ReelRequestDto;
import com.nitish.insta.Payloads.ReelResponseDto;
import org.springframework.data.domain.Page;

public interface ReelService {

    ReelResponseDto createReel(ReelRequestDto reelRequest, String jwtToken) throws Exception;

    ReelResponseDto getReelById(Long postId);

    void deleteReel(Long postId, String jwtToken) throws Exception;
    public ReelResponseDto updateReel(Long postId, ReelRequestDto reelRequest, String jwtToken) throws Exception;

    Page<ReelResponseDto> getAllReels(int page, int size);
}
