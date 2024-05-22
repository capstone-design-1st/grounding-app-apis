package org.example.first.groundingappapis.service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.SmsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{
    @Value("${app.aligo.api-key}")
    private String aligoApiKey;

    @Value("${app.aligo.user-id}")
    private String aligoUserId;

    @Value("${app.aligo.sender-key}")
    private String aligoSenderKey;

    @Value("${app.aligo.tpl-code}")
    private String tpl_code;

    @Value("${app.aligo.sender-phone-number}")
    private String senderPhoneNumber;


    public void sendAlimtalk(SmsDto.MessageDto messageDto) throws RestClientException{

        String receiverPhoneNumber = messageDto.getTo();
        String subject_1 = "[그라운딩]회원가입 인증번호입니다.";
        String message_1 = messageDto.getContent();
        try{
            /*
 button_1: {
"button": [{
"name": "채널 추가",
"linkType": "AC",
"linkTypeName": "채널 추가"
}]
}
 */
            HttpResponse<JsonNode> response = Unirest.post("https://kakaoapi.aligo.in/akv10/alimtalk/send/")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("apikey", aligoApiKey)
                    .field("userid", aligoUserId)
                    .field("senderkey", aligoSenderKey)
                    .field("tpl_code", tpl_code)
                    .field("receiver_1", receiverPhoneNumber)
                    .field("sender", senderPhoneNumber)
                    .field("subject_1", subject_1)
                    .field("message_1", message_1)
                    .field("button_1", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\",\"linkTypeName\":\"채널 추가\"}]}")
                    .asJson();

            if(response.isSuccess()) {
                JSONObject responseBody = response.getBody().getObject();
                int code = responseBody.getInt("code");

                if(code == 0){
                    log.info("알림톡 전송 성공: {}", responseBody.toString());
                } else {
                    throw new RestClientException("알림톡 전송 실패: " + responseBody.toString());
                }
            }
        } catch (Exception e){
            log.error("알림톡 전송 실패: {}", e.getMessage());
        }
    }
}
