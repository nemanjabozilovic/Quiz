package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Question;
import com.example.quiz.domain.models.QuestionDTO;

public class QuestionMapper {
    public static QuestionDTO toDTO(Question question) {
        return new QuestionDTO(question.getId(), question.getText(), question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4(), question.getOption5(), question.getCorrectAnswer(), question.getNumberOfPoints());
    }

    public static Question toModel(QuestionDTO questionDTO) {
        return new Question(questionDTO.getId(), questionDTO.getText(), questionDTO.getOption1(), questionDTO.getOption2(), questionDTO.getOption3(), questionDTO.getOption4(), questionDTO.getOption5(), questionDTO.getCorrectAnswer(), questionDTO.getNumberOfPoints());
    }
}