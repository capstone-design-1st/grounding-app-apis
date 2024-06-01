package org.example.first.groundingappapis.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.*;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.PropertyErrorResult;
import org.example.first.groundingappapis.exception.PropertyException;
import org.example.first.groundingappapis.exception.UserErrorResult;
import org.example.first.groundingappapis.exception.UserException;
import org.example.first.groundingappapis.repository.DayTransactionLogRepository;
import org.example.first.groundingappapis.repository.PropertyRepository;
import org.example.first.groundingappapis.repository.RealTimeTransactionLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final DayTransactionLogRepository dayTransactionLogRepository;
    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> readPropertiesOrderedByVolume(Pageable pageable) {
        return null;
    }

    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> readPropertiesByUserLike(UUID userId, Pageable pageable) {
        return null;
    }

    @Transactional
    @Override
    public PropertyDto.GetResponse getProperty(String propertyId) {
        Property property = propertyRepository.getDetailPropertyById(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        PropertyDto propertyDto = property.toDto();
        FundraiseDto fundraiseDto = property.getFundraise().toDto();
        PropertyDetailDto propertyDetailDto = null;

        if(property.getType().equals("land")) {
            Land land = property.getLand();
            propertyDetailDto = buildLandInformation(land);
        } else {
            Building building = property.getBuilding();
            propertyDetailDto = buildBuildingInformation(building);
        }

        LocationDto locationDto = property.getLocation().toDto();
        ThumbnailUrlDto thumbnailUrlDto = property.getThumbnailUrl().toDto();

        List<NewsDto> newsDto = property.getNews().stream()
                .map(news -> news.toDto())
                .collect(Collectors.toList());

        List<RepresentationPhotoUrlDto> representationPhotoUrlDto = property.getRepresentationPhotoUrls().stream()
                .map(representationPhotoUrl -> representationPhotoUrl.toDto())
                .collect(Collectors.toList());

        List<InvestmentPointDto> investmentPointDto = property.getInvestmentPoints().stream()
                .map(investmentPoint -> investmentPoint.toDto())
                .collect(Collectors.toList());

        List<DocumentDto> documentDto = property.getDocuments().stream()
                .map(document -> document.toDto())
                .collect(Collectors.toList());

        PropertyDto.GetResponse response = PropertyDto.GetResponse.builder()
                .propertyDto(propertyDto)
                .fundraiseDto(fundraiseDto)
                .propertyDetailDto(propertyDetailDto)
                .locationDto(locationDto)
                .thumbnailUrlDto(thumbnailUrlDto)
                .newsDto(newsDto)
                .representationPhotoUrlDto(representationPhotoUrlDto)
                .investmentPointDto(investmentPointDto)
                .documentDto(documentDto)
                .build();

        return response;
    }

    @Override
    public void validateProperty(String propertyId) {

        UUID uuid = UUID.fromString(propertyId);
        Optional<Property> property = propertyRepository.findById(uuid);

        PropertyErrorResult propertyErrorResult;
        if(property.isEmpty()) {
            propertyErrorResult = PropertyErrorResult.PROPERTY_NOT_FOUND;
            throw new PropertyException(propertyErrorResult, propertyErrorResult.getMessage());
        }

    }

    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> getPopularProperties(Pageable pageable) {

        Page<Property> popularProperties = propertyRepository.findAll(pageable);

        List<RealTimeTransactionLogDto> transactionLogs = realTimeTransactionLogRepository
                .findRecentTransactionLogsByProperties(popularProperties.getContent());

        Map<UUID, RealTimeTransactionLogDto> transactionLogMap = transactionLogs.stream()
                .collect(Collectors.toMap(RealTimeTransactionLogDto::getPropertyId, log -> log));

        List<DayTransactionLogDto> dayTransactionLogs = dayTransactionLogRepository
                .findRecentDayTransactionLogsByProperties(popularProperties.getContent());

        Map<UUID, DayTransactionLogDto> dayTransactionLogMap = dayTransactionLogs.stream()
                .collect(Collectors.toMap(DayTransactionLogDto::getPropertyId, log -> log));

        return popularProperties.map(property -> {
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
    public Page<DayTransactionLogDto.ReadResponse> getDayTransactionLog(String propertyId, Pageable pageable) {

        final Property property = propertyRepository.findById(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        Page<DayTransactionLogDto.ReadResponse> dayTransactionLogs = dayTransactionLogRepository
                .readDayTransactionLogsByPropertyId(property.getId(), pageable);

        return dayTransactionLogs;
    }
    @Transactional(readOnly = true)
    @Override
    public Page<PropertyDto.SearchResultResponse> searchProperties(String keyword, Pageable pageable) {

        Page<PropertyDto.SearchResultResponse> searchResult = propertyRepository.searchProperties(keyword, pageable)
                .map(result -> {
                    //UUID propertyId = (UUID) result[0];
                    //UUID propertyId = UUID.nameUUIDFromBytes(((String) result[0]).getBytes());
                    UUID propertyId = UUID.fromString((String) result[0]);
                    String type = (String) result[1];
                    String city = (String) result[2];
                    String gu = (String) result[3];
                    String name = (String) result[4];
                    String oneline = (String) result[5];
                    Double fluctuationRate = (Double) result[6];

                    return PropertyDto.SearchResultResponse.builder()
                            .propertyId(propertyId)
                            .type(type)
                            .city(city)
                            .gu(gu)
                            .name(name)
                            .oneLine(oneline)
                            .fluctuationRate(fluctuationRate)
                            .build();
                });

        return searchResult;
    }

    private PropertyDetailDto buildLandInformation(Land land) {
        return LandDto.landBuilder()
                .useArea(land.getUseArea())
                .mainUse(land.getMainUse())
                .totalFloorArea(land.getTotalFloorArea())
                .landArea(land.getLandArea())
                .scale(land.getScale())
                .completionDate(land.getCompletionDate())
                .officialLandPrice(land.getOfficialLandPrice())
                .leaser(land.getLeaser())
                .leaseStartDate(land.getLeaseStartDate())
                .leaseEndDate(land.getLeaseEndDate())
                .build();
    }

    private PropertyDetailDto buildBuildingInformation(Building building) {
        return BuildingDto.buildingBuilder()
                .useArea(building.getUseArea())
                .mainUse(building.getMainUse())
                .totalFloorArea(building.getTotalFloorArea())
                .landArea(building.getLandArea())
                .scale(building.getScale())
                .completionDate(building.getCompletionDate())
                .officialLandPrice(building.getOfficialLandPrice())
                .leaser(building.getLeaser())
                .leaseStartDate(building.getLeaseStartDate())
                .leaseEndDate(building.getLeaseEndDate())
                .floorCount(building.getFloorCount())
                .build();
    }

    public static Long getPriceDifference(int executedPrice, int openingPrice) {
        return Long.valueOf(executedPrice - openingPrice);
    }
}
