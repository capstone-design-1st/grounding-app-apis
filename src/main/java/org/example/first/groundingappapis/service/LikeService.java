package org.example.first.groundingappapis.service;

import jakarta.transaction.Transactional;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface LikeService {

    PropertyDto.LikePropertyDto likeProperty(UUID propertyId, UUID userId);

    PropertyDto.DislikePropertyDto dislikeProperty(UUID propertyId, UUID userId);

    Page<PropertyDto.ReadBasicInfoResponse> getUserLikedPostList(UUID userId, int size, int page);
}
