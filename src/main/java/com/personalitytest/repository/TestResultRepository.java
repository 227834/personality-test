package com.personalitytest.repository;

import com.personalitytest.model.TestResult;
import com.personalitytest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserOrderByTestDateDesc(User user);
}