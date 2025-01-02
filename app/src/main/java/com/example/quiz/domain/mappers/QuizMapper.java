package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Question;
import com.example.quiz.data.models.Quiz;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuizDTO;

import java.util.List;
import java.util.stream.Collectors;

public class QuizMapper {
    public static QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(), quiz.getQuizName(), quiz.getDate());
    }

    public static Quiz toModel(QuizDTO quizDTO) {
        return new Quiz(quizDTO.getId(), quizDTO.getQuizName(), quizDTO.getDate());
    }

    public static List<QuizDTO> toDTO(List<Quiz> quizzes) {
        return quizzes.stream()
                .map(QuizMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Quiz> toModel(List<QuizDTO> quizDTOList) {
        return quizDTOList.stream()
                .map(QuizMapper::toModel)
                .collect(Collectors.toList());
    }
}
