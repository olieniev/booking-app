package com.olieniev.bookingapp.service.notification;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private static final String CHAT_ID_KEY = "chat_id";
    private static final String SEND_MESSAGE_PATH = "/sendMessage";
    private static final String TEXT_KEY = "text";
    private final RestTemplate restTemplate;
    @Value("${tg.api.base-url}")
    private String baseUrl;
    @Value("${tg.api.token}")
    private String token;
    @Value("${tg.api.chat-id}")
    private String chatIdValue;

    @Override
    public void notify(String message) {
        Map<String,String> request = new HashMap<>();
        request.put(CHAT_ID_KEY, chatIdValue);
        request.put(TEXT_KEY, message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        String url = String.format("%s%s%s", baseUrl, token, SEND_MESSAGE_PATH);
        restTemplate.postForEntity(url, entity, String.class);
    }
}
