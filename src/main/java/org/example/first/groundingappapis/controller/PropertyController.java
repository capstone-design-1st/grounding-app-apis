package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.service.PropertyService;
import org.example.first.groundingappapis.vo.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/properties")
@RestController
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    //매물 상세
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto.GetResponse> getProperty(@PathVariable String propertyId) {
        log.info("getProperty called with propertyId: {}", propertyId);

        return ResponseEntity.ok(propertyService.getProperty(propertyId));
    }
    //거래량이 많은 매물 페이지
    @GetMapping("/popular")
    public ResponseEntity<Page<PropertyDto.ReadBasicInfoResponse>> getPopularProperties(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getPopularProperties called with page: {}, size: {}", page, size);

        final SortType sortType = SortType.VOLUMES;
        final Sort sortBy = Sort.by(Sort.Direction.DESC, SortType.getSortField(sortType));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        return ResponseEntity.ok(propertyService.getPopularProperties(pageable));
    }


    //거래 중인 건물 및 토지 검색 페이지


}
