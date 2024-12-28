package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.QuizDTO;

import java.util.List;

public interface IQuizUseCase {
    QuizDTO getQuizById(int quizId);
    List<QuizDTO> getAllQuizzes();
    boolean insertQuiz(QuizDTO quizDTO);
    boolean updateQuiz(QuizDTO quizDTO);
    boolean deleteQuiz(int quizId);
}
