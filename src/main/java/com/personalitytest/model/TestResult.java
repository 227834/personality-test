package com.personalitytest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String personalityType;

    @Column(nullable = false)
    private String personalityDescription;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "test_date")
    private Long testDate;

    public TestResult(User user, String personalityType, String description, Integer score) {
        this.user = user;
        this.personalityType = personalityType;
        this.personalityDescription = description;
        this.score = score;
        this.testDate = System.currentTimeMillis();
    }
}