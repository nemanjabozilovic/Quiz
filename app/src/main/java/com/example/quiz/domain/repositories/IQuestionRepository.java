package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.Question;

import java.util.List;

public interface IQuestionRepository {
    Question getQuestionById(int questionId);
    List<Question> getAllQuestions();
    boolean insertQuestion(Question question);
    boolean updateQuestion(Question question);
    boolean deleteQuestion(int questionId);
}