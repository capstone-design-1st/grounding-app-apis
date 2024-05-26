package org.example.first.groundingappapis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.Like;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.RealTimeTransactionLog;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.exception.PropertyErrorResult;
import org.example.first.groundingappapis.exception.PropertyException;
import org.example.first.groundingappapis.exception.UserErrorResult;
import org.example.first.groundingappapis.exception.UserException;
import org.example.first.groundingappapis.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.example.first.groundingappapis.service.PropertyServiceImpl.getPriceDifference;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final DayTransactionLogRepository dayTransactionLogRepository;

    @Override
    @Transactional
    public PropertyDto.LikePropertyDto likeProperty(UUID propertyId, UUID userId) {
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        if (likeRepository.existsByPropertyAndUser(property, user)) {
            throw new PropertyException(PropertyErrorResult.ALREADY_LIKED);
        }

        final Like like = Like.builder()
                .property(property)
                .user(user)
                .build();

        final Like savedLike = likeRepository.save(like);

        property.increaseLikeCount();

        final PropertyDto.LikePropertyDto likePropertyDto = PropertyDto.LikePropertyDto.builder()
                .propertyId(property.getId())
                .likeCount(property.getLikeCount())
                .userId(user.getId())
                .likedAt(savedLike.getCreatedAt())
                .build();

        return likePropertyDto;
    }

    @Override
    @Transactional
    public PropertyDto.DislikePropertyDto dislikeProperty(UUID propertyId, UUID userId) {
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Like like = likeRepository.findByPropertyAndUser(property, user).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        likeRepository.delete(like);

        property.decreaseLikeCount();

        final PropertyDto.DislikePropertyDto dislikePropertyDto = PropertyDto.DislikePropertyDto.builder()
                .propertyId(property.getId())
                .likeCount(property.getLikeCount())
                .userId(user.getId())
                .dislikedAt(LocalDateTime.now())
                .build();

        return dislikePropertyDto;
    }

    @Transactional
    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> getUserLikedPostList(UUID userId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        // 유저가 좋아요한 매물 페이징 조회
        Page<Property> userLikedProperties = likeRepository.findAllLikedPropertyByUser(user, pageable);

        // 매물에 대한 가장 최근 거래 로그 조회
        List<RealTimeTransactionLogDto> transactionLogs = realTimeTransactionLogRepository
                .findRecentTransactionLogsByUserAndProperties(user, userLikedProperties.getContent());

        // 매물과 거래 로그를 매핑하여 DTO 생성
        Map<UUID, RealTimeTransactionLogDto> transactionLogMap = transactionLogs.stream()
                .collect(Collectors.toMap(RealTimeTransactionLogDto::getPropertyId, log -> log));

        //매물에 대한 가장 최근 일일 거래 로그 조회
        List<DayTransactionLogDto> dayTransactionLogs = dayTransactionLogRepository
                .findRecentDayTransactionLogsByProperties(userLikedProperties.getContent());

        // 매물과 거래 로그를 매핑하여 DTO 생성
        Map<UUID, DayTransactionLogDto> dayTransactionLogMap = dayTransactionLogs.stream()
                .collect(Collectors.toMap(DayTransactionLogDto::getPropertyId, log -> log));

        // DTO 매핑하여 반환
        return userLikedProperties.map(property -> {
            RealTimeTransactionLogDto transactionLog = transactionLogMap.get(property.getId());
            DayTransactionLogDto dayTransactionLog = dayTransactionLogMap.get(property.getId());
            Integer executedPrice = transactionLog != null ? transactionLog.getExecutedPrice() : 0;
            Integer openingPrice = dayTransactionLog != null ? dayTransactionLog.getOpeningPrice() : 0;
            Double fluctuationRate = transactionLog != null ? transactionLog.getFluctuationRate() : 0.0;

            return PropertyDto.ReadBasicInfoResponse.builder()
                    .id(property.getId())
                    .name(property.getName())
                    .presentPrice(property.getPresentPrice())
                    .priceDifference(getPriceDifference(executedPrice, openingPrice))
                    .fluctuationRate(fluctuationRate)
                    .viewCount(property.getViewCount())
                    .likeCount(property.getLikeCount())
                    .volumeCount(property.getVolumeCount())
                    .type(property.getType())
                    .build();
        });

    }

    @Override
    public PropertyDto.GetLikesResponse isUserLikeProperty(UUID uuid, User user) {
        Property property = propertyRepository.findById(uuid)
                .orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        Like like = likeRepository.findByPropertyAndUser(property, user).orElse(null);

        return PropertyDto.GetLikesResponse.builder()
                .userId(user.getId())
                .propertyId(property.getId())
                .isLike(like != null)
                .createdAt(like != null ? like.getCreatedAt() : null)
                .build();
    }
}
