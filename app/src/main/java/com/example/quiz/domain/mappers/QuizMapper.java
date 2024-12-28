package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Quiz;
import com.example.quiz.domain.models.QuizDTO;

public class QuizMapper {
    public static QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(), quiz.getTotalNumberOfPoints(), quiz.getDate());
    }

    public static Quiz toModel(QuizDTO quizDTO) {
        return new Quiz(quizDTO.getId(), quizDTO.getTotalNumberOfPoints(), quizDTO.getDate());
    }
}
