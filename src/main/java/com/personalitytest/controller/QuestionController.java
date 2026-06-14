package com.personalitytest.controller;

import com.personalitytest.model.Question;
import com.personalitytest.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQuestions() {
        Map<String, Object> response = new HashMap<>();
        List<Question> questions = questionService.getAllQuestions();
        response.put("success", true);
        response.put("questions", questions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initializeQuestions() {
        Map<String, Object> response = new HashMap<>();
        try {
            questionService.initializeQuestions();
            response.put("success", true);
            response.put("message", "Questions initialized successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}