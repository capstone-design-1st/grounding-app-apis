package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.PropertyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PropertyService {

    Page<PropertyDto.ReadBasicInfoResponse> readPropertiesOrderedByVolume(Pageable pageable);
    Page<PropertyDto.ReadBasicInfoResponse> readPropertiesByUserLike(UUID userId, Pageable pageable);
    PropertyDto.GetResponse getProperty(String propertyId);

    void validateProperty(String propertyId);
}
