package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Question;
import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionQuizMapper {
    public static QuestionQuizDTO toDTO(QuestionQuiz questionQuiz) {
        return new QuestionQuizDTO(questionQuiz.getQuestionId(), questionQuiz.getQuizId());
    }

    public static QuestionQuiz toModel(QuestionQuizDTO questionQuizDTO) {
        return new QuestionQuiz(questionQuizDTO.getQuestionId(), questionQuizDTO.getQuizId());
    }

    public static List<QuestionQuizDTO> toDTO(List<QuestionQuiz> questionQuizList) {
        return questionQuizList.stream()
                .map(QuestionQuizMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<QuestionQuiz> toModel(List<QuestionQuizDTO> questionQuizDTOS) {
        return questionQuizDTOS.stream()
                .map(QuestionQuizMapper::toModel)
                .collect(Collectors.toList());
    }
}