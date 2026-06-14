package com.personalitytest.controller;

import com.personalitytest.model.User;
import com.personalitytest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.registerUser(username, password, email);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());

            response.put("success", true);
            response.put("message", "Registration successful!");
            response.put("userId", user.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> user = userService.loginUser(username, password);

        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            session.setAttribute("username", user.get().getUsername());

            response.put("success", true);
            response.put("message", "Login successful!");
            response.put("userId", user.get().getId());
            response.put("username", user.get().getUsername());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        session.invalidate();
        response.put("success", true);
        response.put("message", "Logout successful!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            response.put("loggedIn", true);
            response.put("userId", userId);
            response.put("username", session.getAttribute("username"));
            return ResponseEntity.ok(response);
        } else {
            response.put("loggedIn", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}