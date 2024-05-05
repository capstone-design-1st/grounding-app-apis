package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.*;
import org.example.first.groundingappapis.entity.Building;
import org.example.first.groundingappapis.entity.Land;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.ThumbnailUrl;
import org.example.first.groundingappapis.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    @Override
    public Page<PropertyDto.ReadResponse> getProperties(Pageable pageable) {

        Page<PropertyDto.ReadResponse> responses = propertyRepository.readAllByOrderByCreatedAtDesc(pageable);

        //map, if property의 land가 Null이면 readresponse의 listingInformation을 BuildingDto로 변환, 반대의 경우 LandDto로 변환
        responses.forEach(response -> {
            Property property = propertyRepository.findByUuid(response.getUuid()).orElseThrow(() -> new RuntimeException("Property not found"));

            ListingInformation listingInformation = null;

            if(property.getBuilding() == null) {
                Land land = property.getLand();

                listingInformation = buildLandInformation(land);

            }else if(property.getLand() == null) {
                Building building = property.getBuilding();

                listingInformation = buildBuildingInformation(building);

            }

            if(property.getThumbnailUrl() != null) {
                ThumbnailUrlDto thumbnailUrlDto = ThumbnailUrlDto.builder()
                        .s3Url(property.getThumbnailUrl().getS3Url())
                        .cloudfrontUrl(property.getThumbnailUrl().getCloudfrontUrl())
                        .build();

                response.setThumbnailUrlDto(thumbnailUrlDto);
            }

            response.setListingInformation(listingInformation);
        });

        return responses;
    }

    @Override
    public PropertyDto.ReadResponse getProperty(String propertyId) {
        UUID uuid = UUID.fromString(propertyId);

        Property property = propertyRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Property not found"));

        ListingInformation listingInformation = null;

        if(property.getBuilding() == null) {
            Land land = property.getLand();

            listingInformation = buildLandInformation(land);

        }else if(property.getLand() == null) {
            Building building = property.getBuilding();

            listingInformation = buildBuildingInformation(building);

        }
        PropertyDto.ReadResponse response = propertyRepository.readByUuid(uuid);

        if(property.getThumbnailUrl() != null) {
            ThumbnailUrlDto thumbnailUrlDto = ThumbnailUrlDto.builder()
                    .s3Url(property.getThumbnailUrl().getS3Url())
                    .cloudfrontUrl(property.getThumbnailUrl().getCloudfrontUrl())
                    .build();

            response.setThumbnailUrlDto(thumbnailUrlDto);
        }

        response.setListingInformation(listingInformation);

        return response;
    }

    private ListingInformation buildLandInformation(Land land) {
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

    private ListingInformation buildBuildingInformation(Building building) {
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
                .build();
    }
}
