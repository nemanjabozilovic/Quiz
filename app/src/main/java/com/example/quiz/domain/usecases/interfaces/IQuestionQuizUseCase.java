package com.example.quiz.domain.usecases.interfaces;


import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;

import java.util.List;

public interface IQuestionQuizUseCase {
    List<QuestionDTO> getQuestionsForQuiz(int quizId);
    List<QuestionQuizDTO> getQuizzesForQuestion(int questionId);
    void insertQuestionQuiz(QuestionQuizDTO questionQuizDTO);
    int deleteQuestionQuiz(int questionId, int quizId);
    int getNumberOfQuestionsForQuiz(int quizId);
    int deleteAllQuestionsForQuiz(int quizId);
    boolean updateQuestionsForQuiz(int quizId, List<QuestionQuiz> newQuestions);
}