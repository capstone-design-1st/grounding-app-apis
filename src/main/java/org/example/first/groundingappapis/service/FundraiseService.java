package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.FundraiseDto;
import org.example.first.groundingappapis.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface FundraiseService {
    FundraiseDto.FundraiseResponse fundraiseProperty(String propertyId, FundraiseDto.FundraiseRequest fundraiseRequest, UUID userId);
}
