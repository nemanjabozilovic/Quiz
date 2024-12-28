package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.QuestionQuiz;

import java.util.List;

public interface IQuestionQuizRepository {
    List<QuestionQuiz> getQuestionsForQuiz(int quizId);
    List<QuestionQuiz> getQuizzesForQuestion(int questionId);
    void insertQuestionQuiz(QuestionQuiz questionQuiz);
    void deleteQuestionQuiz(int questionId, int quizId);
}