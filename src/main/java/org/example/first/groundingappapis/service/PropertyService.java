package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PropertyService {

    Page<PropertyDto.ReadBasicInfoResponse> readPropertiesOrderedByVolume(Pageable pageable);
    Page<PropertyDto.ReadBasicInfoResponse> readPropertiesByUserLike(UUID userId, Pageable pageable);
    PropertyDto.GetResponse getProperty(String propertyId);

    void validateProperty(String propertyId);

    Page<PropertyDto.ReadBasicInfoResponse> getPopularProperties(Pageable pageable);

    Page<DayTransactionLogDto.ReadResponse> getDayTransactionLog(String propertyId, Pageable pageable);

    Page<PropertyDto.SearchResultResponse> searchProperties(String keyword, Pageable pageable);

    Page<RealTimeTransactionLogDto.ReadResponse> getRealTimeTransactionLog(String propertyId, Pageable pageable);

    Page<PropertyDto.GetFundraisingResponse> getFundraisingProperties(Pageable pageable);
}
