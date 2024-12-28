package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.UserQuiz;

import java.util.List;

public interface IUserQuizRepository {
    List<UserQuiz> getUsersForQuiz(int quizId);
    List<UserQuiz> getQuizzesForUser(int userId);
    void insertUserQuiz(UserQuiz userQuiz);
    void deleteUserQuiz(int userId, int quizId);
}
