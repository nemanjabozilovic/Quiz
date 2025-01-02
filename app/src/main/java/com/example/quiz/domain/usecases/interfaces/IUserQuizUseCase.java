package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.UserQuizDTO;

import java.util.List;

public interface IUserQuizUseCase {
    List<UserQuizDTO> getUsersForQuiz(int quizId);
    List<UserQuizDTO> getQuizzesForUser(int userId);
    long insertUserQuiz(UserQuizDTO userQuizDTO);
    long deleteUserQuiz(int userId, int quizId);
    List<UserQuizDTO> getAllUserQuizzes();
}