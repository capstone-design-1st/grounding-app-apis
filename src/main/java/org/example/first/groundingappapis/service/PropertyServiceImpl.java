package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.*;
import org.example.first.groundingappapis.entity.Building;
import org.example.first.groundingappapis.entity.Land;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.exception.PropertyErrorResult;
import org.example.first.groundingappapis.exception.PropertyException;
import org.example.first.groundingappapis.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;

    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> readPropertiesOrderedByVolume(Pageable pageable) {
        return null;
    }

    @Override
    public Page<PropertyDto.ReadBasicInfoResponse> readPropertiesByUserLike(UUID userId, Pageable pageable) {
        return null;
    }

    @Override
    public PropertyDto.GetResponse getProperty(String propertyId) {
        Property property = propertyRepository.getDetailPropertyByUuid(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.NOT_FOUND_PROPERTY, PropertyErrorResult.NOT_FOUND_PROPERTY.getMessage()));
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
    public PropertyDto.GetFundraisingResponse getFundraisingProperty(String propertyId) {
        Property property = propertyRepository.getFundraisingPropertyByUuid(UUID.fromString(propertyId)).orElseThrow(() -> new PropertyException(PropertyErrorResult.NOT_FOUND_PROPERTY, PropertyErrorResult.NOT_FOUND_PROPERTY.getMessage()));
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

        List<RepresentationPhotoUrlDto> representationPhotoUrlDto = property.getRepresentationPhotoUrls().stream()
                .map(representationPhotoUrl -> representationPhotoUrl.toDto())
                .collect(Collectors.toList());

        PropertyDto.GetFundraisingResponse response = PropertyDto.GetFundraisingResponse.builder()
                .propertyDto(propertyDto)
                .fundraiseDto(fundraiseDto)
                .propertyDetailDto(propertyDetailDto)
                .locationDto(locationDto)
                .thumbnailUrlDto(thumbnailUrlDto)
                .representationPhotoUrlDto(representationPhotoUrlDto)
                .build();

        return response;
    }



    @Override
    public void validateProperty(String propertyId) {

        UUID uuid = UUID.fromString(propertyId);
        Optional<Property> property = propertyRepository.findByUuid(uuid);

        PropertyErrorResult propertyErrorResult;
        if(property.isEmpty()) {
            propertyErrorResult = PropertyErrorResult.NOT_FOUND_PROPERTY;
            throw new PropertyException(propertyErrorResult, propertyErrorResult.getMessage());
        }

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

}
