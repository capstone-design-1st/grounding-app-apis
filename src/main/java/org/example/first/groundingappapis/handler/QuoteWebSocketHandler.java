package org.example.first.groundingappapis.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.service.TradingService;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteWebSocketHandler extends TextWebSocketHandler {
    private final TradingService tradingService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private UUID propertyId;
    private String direction;
    private int basePrice = 0;
    private int page = 0;
    private int size = 8;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> request = objectMapper.readValue(payload, Map.class);

        propertyId = UUID.fromString((String) request.get("propertyId"));
        basePrice = (Integer) request.get("basePrice");
        page = (Integer) request.get("page");
        size = (Integer) request.get("size");
        direction = (String) request.get("direction");

        sendUpdate();
    }
    @Scheduled(fixedRate = 3000)
    public void sendUpdate() {
        try {
            if (propertyId != null) {
                Page<QuoteDto.ReadResponse> quotes;

                if (direction != null) {
                    if ("upper".equalsIgnoreCase(direction)) {
                        quotes = tradingService.readUpperQuotes(propertyId, basePrice, page, size);
                    } else {
                        quotes = tradingService.readDownQuotes(propertyId, basePrice, page, size);
                    }
                    sendQuotesToAllSessions(quotes);
                } else {
                    Page<QuoteDto.ReadResponse> upperQuotes = tradingService.readUpperQuotes(propertyId, basePrice, page, size);
                    Page<QuoteDto.ReadResponse> downQuotes = tradingService.readDownQuotes(propertyId, basePrice, page, size);

                    List<QuoteDto.ReadResponse> combinedQuotes = Stream.concat(upperQuotes.getContent().stream(), downQuotes.getContent().stream())
                            .collect(Collectors.toList());
                    quotes = new PageImpl<>(combinedQuotes, Pageable.unpaged(), combinedQuotes.size());

                    sendQuotesToAllSessions(quotes);
                }
            }
        } catch (Exception e) {
            sendErrorMessageToAllSessions(e);
            log.error("Failed to send update message", e);
        }
    }

    private void sendQuotesToAllSessions(Page<QuoteDto.ReadResponse> quotes) throws Exception {
        String response = objectMapper.writeValueAsString(quotes);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(response));
            }
        }
    }

    private void sendErrorMessageToAllSessions(Exception e) {
        try {
            String errorMessage = objectMapper.writeValueAsString(Map.of(
                    "status", "error",
                    "message", e.getMessage() != null ? e.getMessage() : "Unknown error"
            ));
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(errorMessage));
                }
            }
        } catch (Exception ex) {
            log.error("Failed to send error message", ex);
        }
    }
}
