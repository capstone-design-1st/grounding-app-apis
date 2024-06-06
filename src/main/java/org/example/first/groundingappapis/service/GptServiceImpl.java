package org.example.first.groundingappapis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.InvestmentPointDto;
import org.example.first.groundingappapis.dto.NewsDto;
import org.example.first.groundingappapis.dto.SummaryDto;
import org.example.first.groundingappapis.entity.Summary;
import org.example.first.groundingappapis.repository.SummaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptServiceImpl implements GptService{

    @Value("${open-ai.api-key}")
    private String apiKey;

    final String URL = "https://api.openai.com/v1/chat/completions";

    private final SummaryRepository summaryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SummaryDto saveSummary(List<NewsDto> newsDtoList, List<InvestmentPointDto> investmentPointDtoList) {
        // Save Summary to DB
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.add("Content-Type", "application/json");

            // 뉴스 제목들을 추출하고 쉼표로 결합
            String newsTitles = newsDtoList.stream()
                    .map(newsDto -> newsDto.getTitle())
                    .collect(Collectors.joining(", "));

            // 투자 포인트 내용을 추출하고 쉼표로 결합
            String investmentPoints = investmentPointDtoList.stream()
                    .map(investmentPointDto -> investmentPointDto.getTitle())
                    .collect(Collectors.joining(", "));

            // 전체 내용을 하나의 문자열로 결합
            String contentToSummarize = "Please summarize the following news articles and investment points in korean, not just arranging:\n\n" +
                    newsTitles + "\n" + investmentPoints;

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", "You are an assistant who receives multiple news titles and investment information about a single real estate property and concisely summarizes them for investment reference."),
                    Map.of("role", "user", "content", contentToSummarize)
            ));

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    String content = (String) message.get("content");

                    //TODO : property 넣어야함
                    Summary summary = Summary.builder().content(content).build();
                    summaryRepository.save(summary);

                    return summary.toDto();
                }

            } else {
                log.error("Error occurred while calling OpenAI API: {}", response.getStatusCode());
            }
        return null;

        }catch(Exception e){
            log.error("Exception occurred while calling OpenAI API", e);
        }

//        try {
//            // Create HTTP headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(apiKey);
//            headers.add("Content-Type", "application/json");
//
//            // Create request body
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("model", "gpt-3.5-turbo");
//            requestBody.put("messages", List.of(
//                    Map.of("role", "system", "content", "You are an investment assistant for a real estate investment platform that responds in Korean. " +
//                            "After receiving an array of news article titles and investment points, " +
//                            "you summarize and organize them together to provide users with investment-related information."),
//                    Map.of("role", "user", "content", "Please summarize the following news articles and investment points:")
//            ));
//
//            // Convert request body to JSON string
//            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
//
//            // Create HTTP entity
//            HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
//
//            // Send POST request
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
//
//            // Parse response
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
//                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
//                if (!choices.isEmpty()) {
//                    Map<String, Object> firstChoice = choices.get(0);
//                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
//                    String summary = (String) message.get("content");
//                    summaryDto = SummaryDto.builder().content(summary).build();
//                }
//            } else {
//                log.error("Error occurred while calling OpenAI API: {}", response.getStatusCode());
//            }
//        } catch (Exception e) {
//            log.error("Exception occurred while calling OpenAI API", e);
//        }
//
//        return summaryDto;
        return null;
    }


}
