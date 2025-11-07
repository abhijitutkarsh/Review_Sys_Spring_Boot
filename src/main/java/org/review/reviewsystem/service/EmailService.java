package org.review.reviewsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import okhttp3.*;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String BREVO_API_KEY = ""; // Use your real Brevo API key
    private static final String BREVO_API_URL = "";
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Sends review request email with review link embedded.
     *
     * @param toEmail      Recipient email
     * @param userName     Name of user
     * @param productTitle Title of product to review
     * @param reviewLink   Link to review page with token
     */
    public void sendReviewRequestEmail(String toEmail, String userName, String productTitle, String reviewLink) {
        String subject = "Please review your recent purchase";
        String textContent = "Hello " + userName + ",\n\n"
                + "Please leave a review for the product: " + productTitle + ".\n"
                + "You can submit your review here: " + reviewLink + "\n\nThank you!";

        // Construct JSON payload as Map for Brevo API
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("sender", Map.of("email", "vute.store@gmail.com", "name", "VUTE"));
        jsonMap.put("to", List.of(Map.of("email", toEmail)));
        jsonMap.put("subject", subject);
        jsonMap.put("textContent", textContent);

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Serialize Map to JSON string
            String jsonBody = mapper.writeValueAsString(jsonMap);

            logger.info("Sending email with JSON payload: {}", jsonBody);

            Request request = new Request.Builder()
                    .url(BREVO_API_URL)
                    .addHeader("api-key", BREVO_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.info("Email sent successfully via Brevo to {}", toEmail);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    logger.error("Error sending email via Brevo. Response code: {} Response body: {}", response.code(), errorBody);
                }
            }
        } catch (IOException e) {
            logger.error("Exception sending email via Brevo: {}", e.getMessage(), e);
        }
    }
}
