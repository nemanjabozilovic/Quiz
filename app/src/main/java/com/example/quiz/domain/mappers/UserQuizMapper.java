package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.UserQuiz;
import com.example.quiz.domain.models.UserQuizDTO;

public class UserQuizMapper {
    public static UserQuizDTO toDTO(UserQuiz userQuiz) {
        return new UserQuizDTO(userQuiz.getId(), userQuiz.getUserId(), userQuiz.getQuizId(), userQuiz.getTotalNumberOfPoints());
    }

    public static UserQuiz toModel(UserQuizDTO userQuizDTO) {
        return new UserQuiz(userQuizDTO.getId(), userQuizDTO.getUserId(), userQuizDTO.getQuizId(), userQuizDTO.getTotalNumberOfPoints());
    }
}