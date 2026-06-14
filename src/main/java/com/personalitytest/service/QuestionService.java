package com.personalitytest.service;

import com.personalitytest.model.Question;
import com.personalitytest.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAllByOrderByQuestionNumberAsc();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void initializeQuestions() {
        // Check if questions already exist
        if (questionRepository.count() > 0) {
            return;
        }

        // Initialize 5 personality test questions
        Question q1 = new Question();
        q1.setQuestionNumber(1);
        q1.setQuestionText("當面對陌生人時，你傾向於：");
        q1.setOptionA("主動打招呼，積極交流（外向）");
        q1.setOptionB("等待他人先說話（內向）");
        q1.setOptionC("有時主動，有時被動（中等）");
        q1.setOptionD("避免交流，保持距離（非常內向）");
        q1.setPersonalityType("Extroversion");

        Question q2 = new Question();
        q2.setQuestionNumber(2);
        q2.setQuestionText("當遇到問題時，你通常：");
        q2.setOptionA("立即尋求他人幫助（感性）");
        q2.setOptionB("自己分析研究解決（理性）");
        q2.setOptionC("先觀察他人做法（中等）");
        q2.setOptionD("忽視問題希望它自行解決（逃避）");
        q2.setPersonalityType("Thinking");

        Question q3 = new Question();
        q3.setQuestionNumber(3);
        q3.setQuestionText("你對新事物的態度是：");
        q3.setOptionA("非常感興趣，想立即嘗試（冒險型）");
        q3.setOptionB("謹慎考慮，確認安全後再試（保守型）");
        q3.setOptionC("視情況而定（平衡型）");
        q3.setOptionD("傾向避免，堅持習慣（守舊型）");
        q3.setPersonalityType("Risk");

        Question q4 = new Question();
        q4.setQuestionNumber(4);
        q4.setQuestionText("工作時你更看重：");
        q4.setOptionA("個人成就和成功（競爭型）");
        q4.setOptionB("與團隊的和諧和協作（合作型）");
        q4.setOptionC("穩定和安全（安全型）");
        q4.setOptionD("自由和自主（獨立型）");
        q4.setPersonalityType("Work");

        Question q5 = new Question();
        q5.setQuestionNumber(5);
        q5.setQuestionText("面對失敗時，你的反應是：");
        q5.setOptionA("快速調整重新開始（樂觀型）");
        q5.setOptionB("感到沮喪，需要時間恢復（悲觀型）");
        q5.setOptionC("理性分析原因改進（理性型）");
        q5.setOptionD("責怪外部因素（逃避型）");
        q5.setPersonalityType("Resilience");

        questionRepository.save(q1);
        questionRepository.save(q2);
        questionRepository.save(q3);
        questionRepository.save(q4);
        questionRepository.save(q5);
    }
}