package org.example.first.groundingappapis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.KafkaDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationProducerImpl implements KafkaNotificationProducer{
    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    /*
    아래 예시는 KafkaDto.UserInfo 객체를 받아서 KafkaProducer를 이용해 Kafka에 메시지를 전송하는 메소드입니다.

    grounding service에서 매물 및 관련 파일 생성 시 토픽을 만들어 정보를 전송해주시면 됩니다.
    토픽을 통해 send하면 자동으로 토픽은 생성됩니다.
    userJsonString처럼 객체를 json string으로 변환하여 전송하면 됩니다.
     */
//    @Override
//    public void createUser(KafkaDto.UserInfo userInfo) {
//        String topic = "user-create";
//
//        String userJsonString;
//        try{
//            //직렬화
//            userJsonString = objectMapper.writeValueAsString(userInfo);
//        } catch (JsonProcessingException e) {
//            log.error("Error while converting user object to json string", e);
//            throw new RuntimeException("Error while converting user object to json string", e);
//        }
//
//        kafkaProducer.send(topic, userJsonString);
//    }

    public void sendNotification(String topic, KafkaDto.NotificationProducingDto notificationProducingDto) {
        String notificationJsonString;
        try {
            notificationJsonString = objectMapper.writeValueAsString(notificationProducingDto);
        } catch (JsonProcessingException e) {
            log.error("Error while converting notification object to json string", e);
            throw new RuntimeException("Error while converting notification object to json string", e);
        }
        kafkaProducer.send(topic, notificationJsonString);
    }
}
