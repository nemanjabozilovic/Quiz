package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.mappers.QuestionQuizMapper;
import com.example.quiz.domain.models.QuestionQuizDTO;
import com.example.quiz.domain.repositories.IQuestionQuizRepository;
import com.example.quiz.domain.usecases.interfaces.IQuestionQuizUseCase;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionQuizUseCase implements IQuestionQuizUseCase {
    private final IQuestionQuizRepository questionQuizRepository;

    public QuestionQuizUseCase(IQuestionQuizRepository questionQuizRepository) {
        this.questionQuizRepository = questionQuizRepository;
    }

    @Override
    public List<QuestionQuizDTO> getQuestionsForQuiz(int quizId) {
        List<QuestionQuiz> questionQuizzes = questionQuizRepository.getQuestionsForQuiz(quizId);
        return questionQuizzes.stream()
                .map(QuestionQuizMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionQuizDTO> getQuizzesForQuestion(int questionId) {
        List<QuestionQuiz> questionQuizzes = questionQuizRepository.getQuizzesForQuestion(questionId);
        return questionQuizzes.stream()
                .map(QuestionQuizMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void insertQuestionQuiz(QuestionQuizDTO questionQuizDTO) {
        QuestionQuiz questionQuiz = QuestionQuizMapper.toModel(questionQuizDTO);
        questionQuizRepository.insertQuestionQuiz(questionQuiz);
    }

    @Override
    public void deleteQuestionQuiz(int questionId, int quizId) {
        questionQuizRepository.deleteQuestionQuiz(questionId, quizId);
    }
}