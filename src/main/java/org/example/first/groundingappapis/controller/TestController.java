package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.NewsDto;
import org.example.first.groundingappapis.dto.InvestmentPointDto;
import org.example.first.groundingappapis.dto.SummaryDto;
import org.example.first.groundingappapis.service.GptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/tests")
@RestController
@RequiredArgsConstructor
public class TestController {

    //test gpt
    private final GptService gptService;

//    @GetMapping("/gpt")
//    public SummaryDto testGpt() {
//        log.info("testGpt called");
// 
//        NewsDto newsDto1 = NewsDto.builder().title("조각투자사 소유, 8호 부동산 \'신도림 핀포인트타워 2호\' 완판").content("content1").build();
//        NewsDto newsDto2 = NewsDto.builder().title("루센트블록 소유, 부동산 상품 \'신도림 핀포인트타워 2호\' 공모 시작").content("content2").build();
//        NewsDto newsDto3 = NewsDto.builder().title("삼성이 노린다?, 부동산 상품 \'신도림 핀포인트타워 2호\' 공모 시작").content("content3").build();
//
//        List<NewsDto> newsDtoList = List.of(newsDto1, newsDto2, newsDto3);
//
//        InvestmentPointDto InvestmentPointDto1 = InvestmentPointDto.builder().title("💰 연 6% 고정 배당금 지급").build();
//        InvestmentPointDto InvestmentPointDto2 = InvestmentPointDto.builder().title("💰 시세 대비 낮은 공모가, 매각 차익 기대").build();
//        InvestmentPointDto InvestmentPointDto3 = InvestmentPointDto.builder().title("💰 신도림역 더블 역세권, 오피스 최적 입지").build();
//
//        List<InvestmentPointDto> InvestmentPointDtoList = List.of(InvestmentPointDto1, InvestmentPointDto2, InvestmentPointDto3);
//
//        return gptService.saveSummary(newsDtoList, InvestmentPointDtoList);
//
//
//    }
}
