package com.personalitytest.controller;

import com.personalitytest.model.TestResult;
import com.personalitytest.model.User;
import com.personalitytest.service.TestResultService;
import com.personalitytest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitTest(
            @RequestBody Map<String, Object> payload,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "User not logged in!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            @SuppressWarnings("unchecked")
            List<Integer> answers = (List<Integer>) payload.get("answers");
            int[] answerArray = answers.stream().mapToInt(i -> i).toArray();

            String personalityType = testResultService.calculatePersonalityType(answerArray);
            String description = testResultService.getPersonalityDescription(personalityType);
            Integer score = answers.stream().mapToInt(i -> i).sum();

            TestResult result = testResultService.saveTestResult(user.get(), personalityType, description, score);

            response.put("success", true);
            response.put("personalityType", personalityType);
            response.put("description", description);
            response.put("score", score);
            response.put("resultId", result.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getTestHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "User not logged in!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<TestResult> results = testResultService.getUserResults(user.get());
            response.put("success", true);
            response.put("results", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}