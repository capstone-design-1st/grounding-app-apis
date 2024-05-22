package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.MailDto;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendMail(MailDto.MailSendDto mailSendDto);
}
