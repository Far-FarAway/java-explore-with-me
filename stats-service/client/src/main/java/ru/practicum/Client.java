package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class Client {
    private RestTemplate rest;

    public Client(@Value("${ru.practicum.client.host}") String host,
                  @Value("${ru.practicum.client.port}") Integer port) {
        this.rest = new RestTemplateBuilder().uriTemplateHandler(new DefaultUriBuilderFactory("http://" + host + ":" + port))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public ResponseEntity<ResponseStatDto> getStats(Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String encodedDate = URLEncoder.encode(params.get("timestamp").toString(), StandardCharsets.UTF_8);
        params.put("timestamp", encodedDate);

        HttpEntity<RequestStatDto> requestEntity = new HttpEntity<>(headers);

        return rest.exchange("/stats", HttpMethod.GET, requestEntity, ResponseStatDto.class, params);
    }

    public ResponseEntity<ResponseStatDto> postStat(RequestStatDto body) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<RequestStatDto> requestEntity = new HttpEntity<>(body, headers);

        return rest.exchange("/hit", HttpMethod.POST, requestEntity, ResponseStatDto.class);
    }
}
