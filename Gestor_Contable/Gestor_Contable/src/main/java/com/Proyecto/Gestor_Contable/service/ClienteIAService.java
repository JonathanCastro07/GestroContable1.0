package com.Proyecto.Gestor_Contable.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Falta la variable de entorno GEMINI_API_KEY");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of("maxOutputTokens", 4000)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = apiUrl + "/" + model + ":generateContent";

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, Map.class
            ).getBody();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            @SuppressWarnings("unchecked")
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el analisis con IA: " + e.getMessage());
        }
    }
}
