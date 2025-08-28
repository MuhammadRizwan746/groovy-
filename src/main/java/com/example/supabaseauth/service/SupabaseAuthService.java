package com.example.supabaseauth.service;

import com.example.supabaseauth.dto.LoginRequest;
import com.example.supabaseauth.dto.SignupRequest;
import com.example.supabaseauth.dto.ProfileResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAuthService {

    private final WebClient webClient;
    private final String anonKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public SupabaseAuthService(@Value("${supabase.url}") String supabaseUrl,
                               @Value("${supabase.anon-key}") String anonKey,
                               WebClient.Builder builder) {
        this.anonKey = anonKey;
        this.webClient = builder.baseUrl(supabaseUrl + "/auth/v1").build();
    }

    public String signup(SignupRequest req) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", req.getEmail());
        body.put("password", req.getPassword());

        try {
            return webClient.post()
                    .uri("/signup")
                    .header("apikey", anonKey)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + anonKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException ex) {
            return ex.getResponseBodyAsString();
        }
    }

    public String login(LoginRequest req) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", req.getEmail());
        body.put("password", req.getPassword());

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/token").queryParam("grant_type","password").build())
                    .header("apikey", anonKey)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + anonKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException ex) {
            return ex.getResponseBodyAsString();
        }
    }

    public ProfileResponse profile(String bearerToken) {
        try {
            String resp = webClient.get()
                    .uri("/user")
                    .header("apikey", anonKey)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(resp);
            String id = root.path("id").asText(null);
            String email = root.path("email").asText(null);
            String role = root.path("role").asText(null);
            boolean emailVerified = false;
            if (root.has("user_metadata") && root.path("user_metadata").has("email_verified")) {
                emailVerified = root.path("user_metadata").path("email_verified").asBoolean(false);
            }
            String createdAt = root.path("created_at").asText(null);

            return new ProfileResponse(id, email, role, emailVerified, createdAt);
        } catch (WebClientResponseException ex) {
            // If Supabase returns an error, throw an exception with body for controller to handle
            throw new RuntimeException("Supabase error: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse profile response", e);
        }
    }
}
