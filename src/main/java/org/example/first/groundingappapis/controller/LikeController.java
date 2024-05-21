package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/likes")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    @GetMapping("/properties/users/{userId}")
    public ResponseEntity<Page<PropertyDto.ReadBasicInfoResponse>> getProperty(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        log.info("get user liked post list called with userId: {}", userId);
        Page<PropertyDto.ReadBasicInfoResponse> userLikedPostList = likeService.getUserLikedPostList(UUID.fromString(userId), size, page);

        return ResponseEntity.ok(userLikedPostList);
    }
}
