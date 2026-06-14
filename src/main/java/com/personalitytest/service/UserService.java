package com.personalitytest.service;

import com.personalitytest.model.User;
import com.personalitytest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, String email) throws Exception {
        // Check if username exists
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already exists!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email already exists!");
        }

        // Validate input
        if (username.length() < 3) {
            throw new Exception("Username must be at least 3 characters long!");
        }

        if (password.length() < 6) {
            throw new Exception("Password must be at least 6 characters long!");
        }

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(password);

        // Create and save user
        User user = new User(username, encryptedPassword, email);
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            // Check if password matches
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            }
        }

        return Optional.empty();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}