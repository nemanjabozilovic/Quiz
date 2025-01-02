package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Question;
import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;

import java.util.ArrayList;
import java.util.List;

public class QuestionConversionUtils {
    public static List<QuestionQuizDTO> convertQuestionDTOsToQuestionQuizDTOs(List<QuestionDTO> questionDTOs) {
        List<QuestionQuizDTO> questionQuizDTOs = new ArrayList<>();
        for (QuestionDTO questionDTO : questionDTOs) {
            QuestionQuizDTO questionQuizDTO = QuestionQuizDTO.fromQuestionDTO(questionDTO);
            questionQuizDTOs.add(questionQuizDTO);
        }
        return questionQuizDTOs;
    }

    public static List<QuestionQuiz> convertQuestionDTOsToQuestionQuiz(List<QuestionDTO> questionDTOs, int quizId) {
        List<QuestionQuiz> questionQuizList = new ArrayList<>();
        for (QuestionDTO questionDTO : questionDTOs) {
            QuestionQuiz questionQuiz = new QuestionQuiz(questionDTO.getId(), quizId);
            questionQuizList.add(questionQuiz);
        }
        return questionQuizList;
    }

    public static List<QuestionQuizDTO> convertQuestionsToQuestionQuizDTOs(List<Question> questions, int quizId) {
        List<QuestionQuizDTO> questionQuizDTOs = new ArrayList<>();
        for (Question question : questions) {
            QuestionQuizDTO questionQuizDTO = new QuestionQuizDTO(question.getId(), quizId);
            questionQuizDTOs.add(questionQuizDTO);
        }
        return questionQuizDTOs;
    }

    public static List<QuestionQuiz> convertQuestionsToQuestionQuiz(List<Question> questions, int quizId) {
        List<QuestionQuiz> questionQuizList = new ArrayList<>();
        for (Question question : questions) {
            QuestionQuiz questionQuiz = new QuestionQuiz(question.getId(), quizId);
            questionQuizList.add(questionQuiz);
        }
        return questionQuizList;
    }
}