package org.example.first.groundingappapis.service.kafka;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaUserInfoConsumerService {
    void createUser(String payload, Acknowledgment acknowledgment);
}