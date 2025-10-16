package com.aiassistant.studyassistant.service;

import com.aiassistant.studyassistant.dto.gemini.GeminiRequest;
import com.aiassistant.studyassistant.dto.gemini.GeminiResponse;
import com.aiassistant.studyassistant.model.Flashcard;
import com.aiassistant.studyassistant.model.QuizQuestion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class LLMService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    /**
     * Constructor-based dependency injection is preferred for required dependencies.
     */
    public LLMService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey
    ) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    // --- PUBLIC METHODS ---

    /**
     * Generates a short summary for the given text.
     */
    public String generateSummary(String text) {
        String prompt = "Summarize the following text in 2–3 concise sentences:\n\n" + text;
        String rawResponse = callGeminiApi(prompt).block();
        return cleanResponse(rawResponse);
    }

    /**
     * Generates multiple-choice quiz questions from text.
     */
    public List<QuizQuestion> generateQuiz(String text) {
        String prompt = """
                Based on the text below, generate exactly 5 multiple-choice questions.
                The output MUST be a JSON array of objects, where each object has:
                "question" (string), "options" (array of 4 strings), and "correctAnswerIndex" (integer 0–3).
                Output JSON only, no explanations.
                """ + "\n\n" + text;

        String rawJson = callGeminiApi(prompt).block();
        String cleanedJson = cleanResponse(rawJson);

        try {
            return objectMapper.readValue(cleanedJson, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("Error parsing quiz JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Generates term-definition flashcards from text.
     */
    public List<Flashcard> generateFlashcards(String text) {
        String prompt = """
                From the text below, generate 5–7 key term-definition pairs.
                Return a JSON array like:
                [{"term": "Term", "definition": "Definition"}].
                Output JSON only, no extra text.
                """ + "\n\n" + text;

        String rawJson = callGeminiApi(prompt).block();
        String cleanedJson = cleanResponse(rawJson);

        try {
            return objectMapper.readValue(cleanedJson, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("Error parsing flashcard JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // --- HELPER METHODS ---

    /**
     * Sends a prompt to the Gemini API and retrieves the text response.
     */
    private Mono<String> callGeminiApi(String prompt) {
    var requestBody = new GeminiRequest(
        List.of(new GeminiRequest.Content(
            List.of(new GeminiRequest.Part(prompt))
        ))
    );

    return webClient.post()
        .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(GeminiResponse.class)
        .map(response -> {
            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                return response.candidates().get(0).content().parts().get(0).text();
            }
            return "";
        })
        .doOnError(error -> {
            System.err.println("Exception calling Gemini API: " + error.getMessage());
        })
        .onErrorReturn("Error: Gemini service failure");
}

    /**
     * Cleans up Markdown or code block formatting from Gemini responses.
     */
    private String cleanResponse(String response) {
        if (response == null) return "";
        return response.replace("```json", "").replace("```", "").trim();
    }
}
