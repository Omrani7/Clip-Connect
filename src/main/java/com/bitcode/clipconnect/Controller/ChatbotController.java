package com.bitcode.clipconnect.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class ChatbotController {

    @Value("uzGG1AquVtzdN0kou82gP1EAgeps53ypV3zYiEKG")
    private String cohereApiKey;

    private static final String COHERE_API_URL = "https://api.cohere.ai/v1/chat";


    @PostMapping("/api/chat")
    public ResponseEntity<String> chatWithChatbot(@RequestBody Map<String, Object> requestPayload) {
        try {
            // Extract user message and chat history from the request payload
            String userMessage = (String) requestPayload.get("userMessage");
            List<Map<String, String>> chatHistory = (List<Map<String, String>>) requestPayload.get("chatHistory");


            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", userMessage+"from this data:");
            requestBody.put("chat_history", chatHistory);
            requestBody.put("connectors", List.of(Map.of("id", "web-search")));

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(cohereApiKey);

            // Make the POST request
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(COHERE_API_URL, requestEntity, Map.class);

            // Extract the chatbot's reply text
            Map responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("text")) {
                String chatbotReply = (String) responseBody.get("text");
                return ResponseEntity.ok(chatbotReply);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid response from Cohere API");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

}
