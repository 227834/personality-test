package com.personalitytest.listener;

import com.personalitytest.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener {

    @Autowired
    private QuestionService questionService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        questionService.initializeQuestions();
    }
}