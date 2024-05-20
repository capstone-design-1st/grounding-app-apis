package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.exception.PropertyErrorResult;
import org.example.first.groundingappapis.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/fundraise")
@RestController
@RequiredArgsConstructor
public class FundraiseController {

    private final PropertyService propertyService;
    // 조각 모집 상세 페이지
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto.GetFundraisingResponse> getFundraisingProperty(@PathVariable("propertyId") String propertyId) {
        log.info("getFundraisingProperty called with propertyId: {}", propertyId);

        propertyService.validateProperty(propertyId);

        PropertyDto.GetFundraisingResponse response = propertyService.getFundraisingProperty(propertyId);

        return ResponseEntity.ok().body(response);
    }

}
