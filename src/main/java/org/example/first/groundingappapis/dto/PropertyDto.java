package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PropertyDto {
    private UUID uuid;
    private String name;
    private String oneline;
    private Long presentPrice;
    private Long viewCount;
    private Long likeCount;
    private Long volumeCount;
    private String type;
    //시가 - 현재가
    private Long priceDifference;
    //시가에서 몇 % 오르고 내렸는지
    private Double priceDifferenceRate;

    @Builder
    public PropertyDto(UUID uuid,
                       String name,
                       String oneline,
                       Long presentPrice,
                       Long viewCount,
                       Long likeCount,
                       Long volumeCount,
                       String type,
                       Long priceDifference,
                       Double priceDifferenceRate) {
        this.uuid = uuid;
        this.name = name != null ? name : "";
        this.oneline = oneline != null ? oneline : "";
        this.presentPrice = presentPrice != null ? presentPrice : 0L;
        this.viewCount = viewCount != null ? viewCount : 0L;
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.volumeCount = volumeCount != null ? volumeCount : 0L;
        this.type = type != null ? type : "";
        this.priceDifference = priceDifference != null ? priceDifference : 0L;
        this.priceDifferenceRate = priceDifferenceRate != null ? priceDifferenceRate : 0.0;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse {
        private PropertyDto propertyDto;
        private FundraiseDto fundraiseDto;
        private PropertyDetailDto propertyDetailDto;
        private NewsDto newsDto;
        private LocationDto locationDto;
        private ThumbnailUrlDto thumbnailUrlDto;
        private List<RepresentationPhotoUrlDto> representationPhotoUrlDto;
        private List<InvestmentPointDto> investmentPointDto;
        private List<DocumentDto> documentDto;

        @Builder
        public GetResponse(PropertyDto propertyDto,
                          FundraiseDto fundraiseDto,
                          PropertyDetailDto propertyDetailDto,
                          NewsDto newsDto,
                          LocationDto locationDto,
                          ThumbnailUrlDto thumbnailUrlDto,
                          List<RepresentationPhotoUrlDto> representationPhotoUrlDto,
                          List<InvestmentPointDto> investmentPointDto,
                          List<DocumentDto> documentDto) {
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
        private PropertyDto propertyDto;
        private FundraiseDto fundraiseDto;
        private PropertyDetailDto propertyDetailDto;
        private LocationDto locationDto;
        private ThumbnailUrlDto thumbnailUrlDto;
        private List<RepresentationPhotoUrlDto> representationPhotoUrlDto;

        @Builder
        public GetFundraisingResponse(PropertyDto propertyDto,
                                      FundraiseDto fundraiseDto,
                                      PropertyDetailDto propertyDetailDto,
                                      LocationDto locationDto,
                                      ThumbnailUrlDto thumbnailUrlDto,
                                      List<RepresentationPhotoUrlDto> representationPhotoUrlDto) {
            this.propertyDto = propertyDto;
            this.fundraiseDto = fundraiseDto;
            this.propertyDetailDto = propertyDetailDto;
            this.locationDto = locationDto;
            this.thumbnailUrlDto = thumbnailUrlDto;
            this.representationPhotoUrlDto = representationPhotoUrlDto;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadBasicInfoResponse{
        private UUID uuid;
        private String name;
        private Long presentPrice;
        //시가 - 현재가
        private Long priceDifference;
        //시가에서 몇 % 오르고 내렸는지
        private Double priceDifferenceRate;
        private Long viewCount;
        private Long likeCount;
        private Long volumeCount;
        private String type;

        @Builder
        public ReadBasicInfoResponse(UUID uuid,
                                     String name,
                                     Long presentPrice,
                                     Long priceDifference,
                                     Double priceDifferenceRate,
                                     Long viewCount,
                                     Long likeCount,
                                     Long volumeCount,
                                     String type) {
            this.uuid = uuid;
            this.name = name;
            this.presentPrice = presentPrice;
            this.priceDifference = priceDifference;
            this.priceDifferenceRate = priceDifferenceRate;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.volumeCount = volumeCount;
            this.type = type;
        }
    }
}
