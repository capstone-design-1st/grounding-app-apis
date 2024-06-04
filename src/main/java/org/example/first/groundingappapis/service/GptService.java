package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.InvestmentPointDto;
import org.example.first.groundingappapis.dto.NewsDto;
import org.example.first.groundingappapis.dto.SummaryDto;

import java.util.List;

public interface GptService {

    SummaryDto saveSummary(List<NewsDto> newsDtoList, List<InvestmentPointDto> investmentPointDtoList);
}
