package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/property")
@RestController
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

//    @GetMapping("/all")
//    public ResponseEntity<Page<PropertyDto.ReadResponse>> getProperties(
//            @RequestParam(value = "page", defaultValue = "0") Integer page,
//            @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        log.info("getProperties called with page: {} and size: {}", page, size);
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        return ResponseEntity.ok(propertyService.getProperties(pageable));
//    }

    // 거래 중인 매물 상세
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto.GetResponse> getProperty(@PathVariable String propertyId) {
        log.info("getProperty called with propertyId: {}", propertyId);

        return ResponseEntity.ok(propertyService.getProperty(propertyId));
    }

    // 최근 찜한 매물 5개

    //거래량이 많은 매물 페이지

    //거래 중인 건물 및 토지 검색 페이지


}
