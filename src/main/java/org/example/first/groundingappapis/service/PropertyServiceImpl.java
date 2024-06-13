package org.example.first.groundingappapis.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.example.first.groundingappapis.exception.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.*;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.repository.DayTransactionLogRepository;
import org.example.first.groundingappapis.repository.PropertyRepository;
import org.example.first.groundingappapis.repository.RealTimeTransactionLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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

        SummaryDto summaryDto = SummaryDto.builder()
                .content(property.getSummary() != null ? property.getSummary().getContent() : "")
                .build();

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

        Optional<RealTimeTransactionLog> realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(property.getId());

        Integer presentPrice = realTimeTransactionLog.isPresent() ? realTimeTransactionLog.get().getExecutedPrice() : property.getFundraise().getIssuePrice();

        Boolean isFundraising = !fundraiseDto.getProgressRate().equals(100.0);

        //propertyDto.toBuilder().priceDifference(getPriceDifference)

        Optional<DayTransactionLog> dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByProperty(property.getId());
        Integer openingPrice = dayTransactionLog.isPresent() ? dayTransactionLog.get().getOpeningPrice() : property.getFundraise().getIssuePrice();

        //변동률
        propertyDto.putPriceDifference(getPriceDifference(presentPrice, openingPrice));
        propertyDto.putPriceDifferenceRate((double) (presentPrice - openingPrice) / openingPrice * 100);

        PropertyDto.GetResponse response = PropertyDto.GetResponse.builder()
                .presentPrice(presentPrice)
                .isFundraising(isFundraising)
                .summaryDto(summaryDto)
                .uploaderWalletAddress(property.getUploaderWalletAddress())
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

        List<Property> popularPropertiesList = popularProperties.getContent();

        List<RealTimeTransactionLog> transactionLogs = new ArrayList<>();

        for(Property property : popularPropertiesList) {
            Optional<RealTimeTransactionLog> realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(property.getId());
            if(realTimeTransactionLog.isPresent()) {
                transactionLogs.add(realTimeTransactionLog.get());
            }
        }
        Map<UUID, RealTimeTransactionLog> transactionLogMap = transactionLogs.stream()
                .collect(Collectors.toMap(RealTimeTransactionLog::getPropertyId, log -> log));

        List<DayTransactionLog> dayTransactionLogs = dayTransactionLogRepository
                .findRecentDayTransactionLogsByPropertyIds(popularProperties.getContent().stream().map(Property::getId).collect(Collectors.toList()));

        Map<UUID, DayTransactionLogDto> dayTransactionLogMap = dayTransactionLogs.stream()
                .collect(Collectors.toMap(DayTransactionLog::getPropertyId, log -> log.toDto()));


        return popularProperties.map(property -> {
            RealTimeTransactionLog transactionLog = transactionLogMap.get(property.getId());
            DayTransactionLogDto dayTransactionLog = dayTransactionLogMap.get(property.getId());
            Integer executedPrice = transactionLog != null ? transactionLog.getExecutedPrice() : property.getFundraise() != null ? property.getFundraise().getIssuePrice() : 0;
            Integer openingPrice = dayTransactionLog != null ? dayTransactionLog.getOpeningPrice() : property.getFundraise() != null ? property.getFundraise().getIssuePrice() : 0;
            Double fluctuationRate = transactionLog != null ? transactionLog.getFluctuationRate() : 0.0;

            return PropertyDto.ReadBasicInfoResponse.builder()
                    .id(property.getId())
                    .name(property.getName())
                    .presentPrice(Long.valueOf(executedPrice))
                    .priceDifference(getPriceDifference(executedPrice, openingPrice))
                    .fluctuationRate(fluctuationRate)
                    .viewCount(property.getViewCount())
                    .likeCount(property.getLikeCount())
                    .totalVolume(property.getTotalVolume())
                    .type(property.getType())
                    .build();
        });
    }

    @Override
    public Page<DayTransactionLogDto.ReadResponse> getDayTransactionLog(String propertyId, Pageable pageable) {

        final Property property = propertyRepository.findById(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        final Fundraise fundraise = property.getFundraise();
        if(fundraise.getProgressRate() < 100) {
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        Page<DayTransactionLogDto.ReadResponse> dayTransactionLogs = dayTransactionLogRepository
                .readDayTransactionLogsByPropertyId(property.getId(), pageable);

        return dayTransactionLogs;
    }

    @Override
    public Page<DayTransactionLogDto.ReadResponse> getDayTransactionLogByPropertyName(String propertyName, Pageable pageable) {
        final Property property = propertyRepository.findByName(propertyName).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        final Fundraise fundraise = property.getFundraise();
        if(fundraise.getProgressRate() < 100) {
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        Page<DayTransactionLogDto.ReadResponse> dayTransactionLogs = dayTransactionLogRepository
                .readDayTransactionLogsByPropertyId(property.getId(), pageable);

        return dayTransactionLogs;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PropertyDto.SearchResultResponse> searchProperties(String keyword, Pageable pageable) {
        Page<PropertyDto.SearchResultResponse> searchResult = null;
        if(keyword == null || keyword.isEmpty()) {
             searchResult = propertyRepository.searchProperties(pageable)
                    .map(result -> {
                        UUID propertyId = UUID.fromString((String) result[0]);
                        String type = (String) result[1];
                        String city = (String) result[2];
                        String gu = (String) result[3];
                        String name = (String) result[4];
                        String oneline = (String) result[5];
                        Double fluctuationRate;
                        if(result[6] == null)
                            fluctuationRate = 0.0;
                        else
                            fluctuationRate = (Double) result[6];

                        return PropertyDto.SearchResultResponse.builder()
                                .propertyId(propertyId)
                                .type(type)
                                .city(city)
                                .gu(gu)
                                .name(name)
                                .oneline(oneline)
                                .fluctuationRate(fluctuationRate)
                                .build();
                    });
        }else {
            searchResult = propertyRepository.searchProperties(keyword, pageable)
                    .map(result -> {
                        //UUID propertyId = (UUID) result[0];
                        //UUID propertyId = UUID.nameUUIDFromBytes(((String) result[0]).getBytes());
                        UUID propertyId = UUID.fromString((String) result[0]);
                        String type = (String) result[1];
                        String city = (String) result[2];
                        String gu = (String) result[3];
                        String name = (String) result[4];
                        String oneline = (String) result[5];
                        Double fluctuationRate;
                        if(result[6] == null)
                            fluctuationRate = 0.0;
                        else
                            fluctuationRate = (Double) result[6];

                        return PropertyDto.SearchResultResponse.builder()
                                .propertyId(propertyId)
                                .type(type)
                                .city(city)
                                .gu(gu)
                                .name(name)
                                .oneline(oneline)
                                .fluctuationRate(fluctuationRate)
                                .build();
                    });
        }


        return searchResult;
    }


    @Override
    public Page<RealTimeTransactionLogDto.ReadResponse> getRealTimeTransactionLog(String propertyId, Pageable pageable) {

        final Property property = propertyRepository.findById(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        final Fundraise fundraise = property.getFundraise();
        if(fundraise.getProgressRate() < 100) {
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        Page<RealTimeTransactionLogDto.ReadResponse> realTimeTransactionLogs = realTimeTransactionLogRepository
                .readRealTimeTransactionLogsByPropertyAndExecutedAtToday(property, startOfDay, endOfDay, pageable);

        return realTimeTransactionLogs;
    }

    @Override
    public Page<PropertyDto.GetFundraisingResponse> getFundraisingProperties(Pageable pageable) {
        
        Page<PropertyDto.GetFundraisingResponse> response = propertyRepository.readBasicInfoOfFundraisingProperty(pageable);

        return response;
    }

    private PropertyDetailDto buildLandInformation(Land land) {
        return LandDto.landBuilder()
                .area(land.getArea())
                .landUse(land.getLandUse())
                .etc(land.getEtc())
                .recommendUse(land.getRecommendUse())
                .parking(land.isParking())
                .nearestStation(land.getNearestStation())
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
