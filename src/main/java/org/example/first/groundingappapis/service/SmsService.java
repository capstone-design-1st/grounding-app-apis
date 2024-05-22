package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.SmsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public interface SmsService {
    //SmsDto.SmsResponseDto sendSms(SmsDto.MessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;

    void sendAlimtalk(SmsDto.MessageDto messageDto) throws RestClientException;
}
