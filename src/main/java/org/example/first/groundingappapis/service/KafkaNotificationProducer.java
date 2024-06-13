package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.KafkaDto;

public interface KafkaNotificationProducer {
    void sendNotification(String topic, KafkaDto.NotificationProducingDto notificationProducingDto);
}
