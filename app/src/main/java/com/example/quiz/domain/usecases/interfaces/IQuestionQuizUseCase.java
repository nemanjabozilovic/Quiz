package com.example.quiz.domain.usecases.interfaces;


import com.example.quiz.domain.models.QuestionQuizDTO;

import java.util.List;

public interface IQuestionQuizUseCase {
    List<QuestionQuizDTO> getQuestionsForQuiz(int quizId);
    List<QuestionQuizDTO> getQuizzesForQuestion(int questionId);
    void insertQuestionQuiz(QuestionQuizDTO questionQuizDTO);
    void deleteQuestionQuiz(int questionId, int quizId);
}