package com.example.user.updater.extservice;

import com.example.user.updater.extservice.dto.ExternalServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ExtServiceClient {

    private final RestClient restClient;

    @Value("${ext-service.url}")
    private String extServiceUrl;

    @Value("${ext-service.path.get-user}")
    private String getUserPath;

    public ExternalServiceDto getUser(String login) {
        return restClient.get()
                .uri(extServiceUrl + getUserPath + "/" + login)
                .header("Content-Type", "application/json")
                .retrieve()
                .body(ExternalServiceDto.class);
    }
}
