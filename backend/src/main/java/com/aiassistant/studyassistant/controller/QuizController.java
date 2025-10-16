

package com.aiassistant.studyassistant.controller;

import com.aiassistant.studyassistant.dto.QuizAttemptRequest;
import com.aiassistant.studyassistant.dto.QuizRequest;
import com.aiassistant.studyassistant.model.QuizAttempt;
import com.aiassistant.studyassistant.model.QuizQuestion;
import com.aiassistant.studyassistant.service.LLMService;
import com.aiassistant.studyassistant.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private LLMService llmService;

    @Autowired
    private QuizService quizService;

    // Generates a new quiz
    @PostMapping
    public ResponseEntity<List<QuizQuestion>> generateQuiz(@RequestBody QuizRequest request) {
        return ResponseEntity.ok(llmService.generateQuiz(request.getText()));
    }

    // Saves a quiz attempt
    @PostMapping("/save")
    public ResponseEntity<?> saveQuizAttempt(@RequestBody QuizAttemptRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        quizService.saveQuizAttempt(request, authentication.getName());
        return ResponseEntity.ok("Quiz attempt saved successfully.");
    }

    // Gets all saved quiz attempts for the user
    @GetMapping("/history")
    public ResponseEntity<List<QuizAttempt>> getQuizHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(quizService.getQuizHistoryForUser(authentication.getName()));
    }

    // --- NEW ENDPOINT ---
    // Deletes a specific quiz attempt by its ID
    @DeleteMapping("/{attemptId}")
    public ResponseEntity<?> deleteQuizAttempt(@PathVariable String attemptId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        quizService.deleteQuizAttempt(attemptId, authentication.getName());
        return ResponseEntity.ok("Quiz attempt deleted successfully.");
    }
}
