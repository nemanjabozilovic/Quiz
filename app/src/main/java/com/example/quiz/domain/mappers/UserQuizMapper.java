package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.UserQuiz;
import com.example.quiz.domain.models.UserQuizDTO;

public class UserQuizMapper {
    public static UserQuizDTO toDTO(UserQuiz userQuiz) {
        return new UserQuizDTO(userQuiz.getUserId(), userQuiz.getQuizId());
    }

    public static UserQuiz toModel(UserQuizDTO userQuizDTO) {
        return new UserQuiz(userQuizDTO.getUserId(), userQuizDTO.getQuizId());
    }
}