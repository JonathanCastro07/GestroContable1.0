package com.Proyecto.Gestor_Contable.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ClienteIAService {

    @Value("${ia.api.key}")
    private String apiKey;

    @Value("${ia.api.url}")
    private String apiUrl;

    @Value("${ia.api.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generarTexto(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", 500,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, request, Map.class
            ).getBody();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            return (String) content.get(0).get("text");

        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el analisis con IA: " + e.getMessage());
        }
    }
}
