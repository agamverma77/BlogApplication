package org.studyeasy.SpringStarterMVCProject.services;

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

    public String generateSummary(String postBody) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty() || geminiApiKey.equalsIgnoreCase("YOUR_API_KEY")) {
            logger.debug("Gemini API Key is not configured. Skipping AI summary generation.");
            return null;
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey.trim();
            
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

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

            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(requestBody);

            HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && !candidates.isEmpty()) {
                    JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
                    if (!textNode.isMissingNode() && !textNode.asText().trim().isEmpty()) {
                        return textNode.asText().trim();
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Could not generate AI summary: {}", e.getMessage());
            return null;
        }
        
        return null;
    }
}
