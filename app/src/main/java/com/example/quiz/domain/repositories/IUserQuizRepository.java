package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.UserQuiz;

import java.util.List;

public interface IUserQuizRepository {
    List<UserQuiz> getUsersForQuiz(int quizId);
    List<UserQuiz> getQuizzesForUser(int userId);
    long insertUserQuiz(UserQuiz userQuiz);
    long deleteUserQuiz(int userId, int quizId);
    List<UserQuiz> getAllUserQuizzes();
}
