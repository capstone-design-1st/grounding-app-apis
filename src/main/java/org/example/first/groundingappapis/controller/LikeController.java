package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/likes")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/properties")
    public ResponseEntity<Page<PropertyDto.ReadBasicInfoResponse>> getProperty(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {

        UUID userId = user.getUser().getId();

        log.info("get user liked post list called with userId: {}", userId);
        Page<PropertyDto.ReadBasicInfoResponse> userLikedPostList = likeService.getUserLikedPostList(userId, size, page);

        return ResponseEntity.ok(userLikedPostList);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/properties/{propertyId}")
    public ResponseEntity<PropertyDto.LikePropertyDto> likeProperty(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable String propertyId) {

        UUID userId = user.getUser().getId();

        log.info("like property called with userId: {}, propertyId: {}", userId, propertyId);
        PropertyDto.LikePropertyDto likePropertyResponse = likeService.likeProperty(UUID.fromString(propertyId), userId);

        return ResponseEntity.ok(likePropertyResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/properties/{propertyId}")
    public ResponseEntity<PropertyDto.DislikePropertyDto> dislikeProperty(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable String propertyId) {

        UUID userId = user.getUser().getId();

        log.info("dislike property called with userId: {}, propertyId: {}", userId, propertyId);
        PropertyDto.DislikePropertyDto dislikePropertyResponse = likeService.dislikeProperty(UUID.fromString(propertyId), userId);

        return ResponseEntity.ok(dislikePropertyResponse);
    }
}
