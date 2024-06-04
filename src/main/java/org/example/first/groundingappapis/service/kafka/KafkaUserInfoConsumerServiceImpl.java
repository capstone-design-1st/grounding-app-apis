package org.example.first.groundingappapis.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.KafkaMessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaUserInfoConsumerServiceImpl implements KafkaUserInfoConsumerService{
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "user-create")
    @Transactional
    public void createUser(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        KafkaMessageDto.UserInfoDto userInfoDto = null;

        try {
            // 역직렬화
            userInfoDto = objectMapper.readValue(payload, KafkaMessageDto.UserInfoDto.class);
        } catch (Exception e) {
            log.error("Error while converting json string to user object", e);
        }

        log.info("user name: {}, email: {}", userInfoDto.getName(), userInfoDto.getEmail());
        acknowledgment.acknowledge();

    }
}