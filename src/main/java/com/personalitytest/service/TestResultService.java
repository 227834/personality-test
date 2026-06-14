package com.personalitytest.service;

import com.personalitytest.model.TestResult;
import com.personalitytest.model.User;
import com.personalitytest.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    public TestResult saveTestResult(User user, String personalityType, String description, Integer score) {
        TestResult result = new TestResult(user, personalityType, description, score);
        return testResultRepository.save(result);
    }

    public List<TestResult> getUserResults(User user) {
        return testResultRepository.findByUserOrderByTestDateDesc(user);
    }

    public String calculatePersonalityType(int[] answers) {
        // Simple scoring system
        // A=3, B=0, C=1.5, D=-3
        int score = 0;
        String type = "";

        // Score calculation
        for (int answer : answers) {
            score += answer;
        }

        if (score >= 10) {
            type = "外向樂觀理性冒險者";
        } else if (score >= 5) {
            type = "平衡全能型";
        } else if (score >= 0) {
            type = "謹慎穩定型";
        } else {
            type = "內向保守型";
        }

        return type;
    }

    public String getPersonalityDescription(String personalityType) {
        return switch (personalityType) {
            case "外向樂觀理性冒險者" -> "你是一個充滿活力、樂於探索的人。你喜歡社交，勇於嘗試新事物，善於解決問題。你的樂觀態度和冒險精神會帶領你走向成功。";
            case "平衡全能型" -> "你是一個各方面均衡發展的人。你既有社交能力，也有獨立思考能力。你能很好地在穩定和變化之間找到平衡點。";
            case "謹慎穩定型" -> "你是一個理性謹慎的人。你重視安全和穩定，做事前會充分思考。你的謹慎態度使你避免許多不必要的風險。";
            case "內向保守型" -> "你是一個深思熟慮的人。你傾向於內省和獨立思考，不喜歡盲目跟風。你需要建立自信，相信自己的判斷力。";
            default -> "測驗結果異常，請重新測驗。";
        };
    }
}