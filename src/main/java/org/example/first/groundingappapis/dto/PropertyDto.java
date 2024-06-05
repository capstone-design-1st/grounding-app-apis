package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PropertyDto {
    private UUID id;
    private String name;
    private String oneline;
    private Long viewCount;
    private Long likeCount;
    private Long totalVolume;
    private String type;
    //시가 - 현재가
    private Long priceDifference;
    //시가에서 몇 % 오르고 내렸는지
    private Double priceDifferenceRate;

    @Builder
    public PropertyDto(UUID id,
                       String name,
                       String oneline,
                       Long viewCount,
                       Long likeCount,
                       Long totalVolume,
                       String type,
                       Long priceDifference,
                       Double priceDifferenceRate) {
        this.id = id;
        this.name = name != null ? name : "";
        this.oneline = oneline != null ? oneline : "";
        this.viewCount = viewCount != null ? viewCount : 0L;
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.totalVolume = totalVolume != null ? totalVolume : 0L;
        this.type = type != null ? type : "";
        this.priceDifference = priceDifference != null ? priceDifference : 0L;
        this.priceDifferenceRate = priceDifferenceRate != null ? priceDifferenceRate : 0.0;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse {
        private Integer presentPrice;
        private Boolean isFundraising;
        private PropertyDto propertyDto;
        private FundraiseDto fundraiseDto;
        private PropertyDetailDto propertyDetailDto;
        private LocationDto locationDto;
        private ThumbnailUrlDto thumbnailUrlDto;
        private List<NewsDto> newsDto;
        private List<RepresentationPhotoUrlDto> representationPhotoUrlDto;
        private List<InvestmentPointDto> investmentPointDto;
        private List<DocumentDto> documentDto;

        @Builder
        public GetResponse(
                Integer presentPrice,
                Boolean isFundraising,
                          PropertyDto propertyDto,
                          FundraiseDto fundraiseDto,
                          PropertyDetailDto propertyDetailDto,
                          LocationDto locationDto,
                          ThumbnailUrlDto thumbnailUrlDto,
                           List<NewsDto> newsDto,
                          List<RepresentationPhotoUrlDto> representationPhotoUrlDto,
                          List<InvestmentPointDto> investmentPointDto,
                          List<DocumentDto> documentDto) {
            this.presentPrice = presentPrice;
            this.isFundraising = isFundraising;
            this.propertyDto = propertyDto;
            this.fundraiseDto = fundraiseDto;
            this.propertyDetailDto = propertyDetailDto;
            this.newsDto = newsDto;
            this.locationDto = locationDto;
            this.thumbnailUrlDto = thumbnailUrlDto;
            this.representationPhotoUrlDto = representationPhotoUrlDto;
            this.investmentPointDto = investmentPointDto;
            this.documentDto = documentDto;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetFundraisingResponse {
        private LocalDateTime createdAt;
        private UUID propertyId;
        private String name;
        private String oneline;
        private String thumbnailUrl;

        @Builder
        public GetFundraisingResponse(LocalDateTime createdAt, UUID propertyId, String name, String oneline, String thumbnailUrl) {
            this.createdAt = createdAt;
            this.propertyId = propertyId;
            this.name = name;
            this.oneline = oneline;
            this.thumbnailUrl = thumbnailUrl;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadBasicInfoResponse{
        private UUID id;
        private String name;
        private Long presentPrice;
        //시가 - 현재가, 계산 필요
        private Long priceDifference;
        //시가에서 몇 % 오르고 내렸는지, 계산 필요, Quote에서 가져옴
        private Double fluctuationRate;
        private Long viewCount;
        private Long likeCount;
        private Long totalVolume;
        private String type;

        @Builder
        public ReadBasicInfoResponse(UUID id,
                                     String name,
                                     Long presentPrice,
                                     Long priceDifference,
                                     Double fluctuationRate,
                                     Long viewCount,
                                     Long likeCount,
                                     Long totalVolume,
                                     String type) {
            this.id = id;
            this.name = name;
            this.presentPrice = presentPrice;
            this.priceDifference = priceDifference;
            this.fluctuationRate = fluctuationRate;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.totalVolume = totalVolume;
            this.type = type;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LikePropertyDto {
        private UUID propertyId;
        private UUID userId;
        private LocalDateTime likedAt;
        private Long likeCount;

        @Builder
        public LikePropertyDto(UUID propertyId, UUID userId, LocalDateTime likedAt, Long likeCount) {
            this.propertyId = propertyId;
            this.userId = userId;
            this.likedAt = likedAt;
            this.likeCount = likeCount;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DislikePropertyDto {
        private UUID propertyId;
        private UUID userId;
        private LocalDateTime dislikedAt;
        private Long likeCount;

        @Builder
        public DislikePropertyDto(UUID propertyId, UUID userId, LocalDateTime dislikedAt, Long likeCount) {
            this.propertyId = propertyId;
            this.userId = userId;
            this.dislikedAt = dislikedAt;
            this.likeCount = likeCount;
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class GetLikesResponse {
        private UUID userId;
        private UUID propertyId;
        private Boolean isLike;
        private LocalDateTime createdAt;

        @Builder
        public GetLikesResponse(UUID userId, UUID propertyId, Boolean isLike, LocalDateTime createdAt) {
            this.userId = userId;
            this.propertyId = propertyId;
            this.isLike = isLike;
            this.createdAt = createdAt;
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class SearchResultResponse {
        private UUID propertyId;
        private String type;
        private String city;
        private String gu;
        private String name;
        private String oneline;
        private Double fluctuationRate;

        @Builder
        public SearchResultResponse(UUID propertyId, String type, String city, String gu, String name, String oneline, Double fluctuationRate) {
            this.propertyId = propertyId;
            this.type = type;
            this.city = city;
            this.gu = gu;
            this.name = name;
            this.oneline = oneline;
            this.fluctuationRate = fluctuationRate;
        }
    }
}
