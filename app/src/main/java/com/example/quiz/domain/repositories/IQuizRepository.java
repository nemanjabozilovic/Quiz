package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.Quiz;

import java.util.List;

public interface IQuizRepository {
    Quiz getQuizById(int quizId);
    List<Quiz> getAllQuizzes();
    boolean insertQuiz(Quiz quiz);
    boolean updateQuiz(Quiz quiz);
    boolean deleteQuiz(int quizId);
}
