package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.QuestionQuiz;

import java.util.List;

public interface IQuestionQuizRepository {
    List<QuestionQuiz> getQuestionsForQuiz(int quizId);
    List<QuestionQuiz> getQuizzesForQuestion(int questionId);
    void insertQuestionQuiz(QuestionQuiz questionQuiz);
    int deleteQuestionQuiz(int questionId, int quizId);
    int getNumberOfQuestionsForQuiz(int quizId);
    int deleteAllQuestionsForQuiz(int quizId);
    boolean updateQuestionsForQuiz(int quizId, List<QuestionQuiz> newQuestions);
}