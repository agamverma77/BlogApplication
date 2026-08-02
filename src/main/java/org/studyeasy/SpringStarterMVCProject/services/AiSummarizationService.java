package org.studyeasy.SpringStarterMVCProject.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiSummarizationService {

    private static final Logger logger = LoggerFactory.getLogger(AiSummarizationService.class);

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    private static final List<String> CANDIDATE_MODELS = Arrays.asList(
        "gemini-3.5-flash",
        "gemini-2.5-flash",
        "gemini-2.0-flash",
        "gemini-1.5-flash"
    );

    public String generateSummary(String postBody) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty() || geminiApiKey.equalsIgnoreCase("YOUR_API_KEY")) {
            logger.debug("Gemini API Key is not configured. Skipping AI summary generation.");
            return null;
        }

        String cleanBody = postBody != null ? postBody.replaceAll("<[^>]*>", " ").replaceAll("&nbsp;", " ").replaceAll("\\s+", " ").trim() : "";
        if (cleanBody.isEmpty()) {
            return null;
        }

        String prompt = "Please provide a brief summary (TL;DR) in 1-3 sentences of the following blog post text:\n\n" + cleanBody;

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        String jsonRequest;
        try {
            jsonRequest = mapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            logger.warn("Failed to serialize AI request body: {}", e.getMessage());
            return null;
        }

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        // Try models in order: gemini-3.5-flash first, followed by fallbacks
        for (String modelName : CANDIDATE_MODELS) {
            try {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + geminiApiKey.trim();
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JsonNode root = mapper.readTree(response.getBody());
                    JsonNode candidates = root.path("candidates");
                    if (candidates.isArray() && !candidates.isEmpty()) {
                        JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
                        if (!textNode.isMissingNode() && !textNode.asText().trim().isEmpty()) {
                            logger.info("Successfully generated AI summary with model: {}", modelName);
                            return textNode.asText().trim();
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug("Attempt with model {} was unsuccessful, trying next candidate...", modelName);
            }
        }

        logger.warn("All candidate Gemini models failed to generate summary.");
        return null;
    }
}
