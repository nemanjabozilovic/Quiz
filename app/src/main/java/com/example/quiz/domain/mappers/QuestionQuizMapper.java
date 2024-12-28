package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.models.QuestionQuizDTO;

public class QuestionQuizMapper {
    public static QuestionQuizDTO toDTO(QuestionQuiz questionQuiz) {
        return new QuestionQuizDTO(questionQuiz.getQuestionId(), questionQuiz.getQuizId());
    }

    public static QuestionQuiz toModel(QuestionQuizDTO questionQuizDTO) {
        return new QuestionQuiz(questionQuizDTO.getQuestionId(), questionQuizDTO.getQuizId());
    }
}