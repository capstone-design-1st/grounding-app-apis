package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.PropertyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyService {

    Page<PropertyDto.ReadResponse> getProperties(Pageable pageable);
    PropertyDto.ReadResponse getProperty(String propertyId);
}
