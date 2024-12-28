package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.QuestionDTO;

import java.util.List;

public interface IQuestionUseCase {
    QuestionDTO getQuestionById(int questionId);
    List<QuestionDTO> getAllQuestions();
    boolean insertQuestion(QuestionDTO questionDTO);
    boolean updateQuestion(QuestionDTO questionDTO);
    boolean deleteQuestion(int questionId);
}