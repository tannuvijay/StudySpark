package com.aiassistant.studyassistant.controller;

import com.aiassistant.studyassistant.dto.SummaryRequest;
import com.aiassistant.studyassistant.service.LLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/summaries")
@CrossOrigin(origins = "http://localhost:3000")  
public class SummaryController {

    @Autowired
    private LLMService llmService;

    @PostMapping
    public ResponseEntity<Map<String, String>> getSummary(@RequestBody SummaryRequest request) {
        String summary = llmService.generateSummary(request.getText());
        return ResponseEntity.ok(Map.of("summary", summary));
    }
}
