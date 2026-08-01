package org.studyeasy.SpringStarterMVCProject.services;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

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

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateSummary(String postBody) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty() || geminiApiKey.equals("YOUR_API_KEY")) {
            return "AI Summary is unavailable because the API key is not configured.";
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=" + geminiApiKey;
            
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String cleanBody = postBody != null ? postBody.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim() : "";
            String prompt = "Please provide a brief summary (TL;DR) in 1-3 sentences of the following blog post text:\n\n" + cleanBody;
            
            // Constructing the request payload
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
                JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
                if (!textNode.isMissingNode()) {
                    return textNode.asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating AI summary: " + e.getMessage();
        }
        
        return "Could not generate summary.";
    }
}
