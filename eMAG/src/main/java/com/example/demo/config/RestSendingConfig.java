package com.example.demo.config;



import com.example.demo.entities.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestSendingConfig {
    public static final String FIRST_TOKEN = "1ceb849e-53ea-40de-aa03-e9920e334f0-";
    public static final String SECOND_TOKEN = "4db5ac0d-afe9-4e81-8f50-6c64bf0a0fbc";
    public static final  String authToken=FIRST_TOKEN+SECOND_TOKEN;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RestSendingConfig(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEmailToMailApp(EmailRequest emailRequestDto) {
        try {
            // Convert EmailRequestDto to JSON string
            String jsonPayload = objectMapper.writeValueAsString(emailRequestDto);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + authToken); // Include the authorization token here

            // Set up request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            // Make the HTTP POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/emails/sendEmail", // URL of your email app
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Handle response
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Email successfully sent!");
            } else {
                System.out.println("Failed to send email. Response: " + response.getBody());
            }
        } catch (JsonProcessingException e) {
            // Handle JSON processing exception
            e.printStackTrace();
        }
    }
}


