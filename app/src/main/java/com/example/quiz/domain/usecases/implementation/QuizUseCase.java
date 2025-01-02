package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.Quiz;
import com.example.quiz.domain.mappers.QuestionMapper;
import com.example.quiz.domain.mappers.QuizMapper;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.repositories.IQuestionQuizRepository;
import com.example.quiz.domain.repositories.IQuizRepository;
import com.example.quiz.domain.usecases.interfaces.IQuizUseCase;

import java.util.ArrayList;
import java.util.List;

public class QuizUseCase implements IQuizUseCase {
    private final IQuizRepository quizRepository;
    private final IQuestionQuizRepository questionQuizRepository;

    public QuizUseCase(IQuizRepository quizRepository, IQuestionQuizRepository questionQuizRepository) {
        this.quizRepository = quizRepository;
        this.questionQuizRepository = questionQuizRepository;
    }

    @Override
    public QuizDTO getQuizById(int quizId) {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz != null) {
            return QuizMapper.toDTO(quiz);
        }
        return null;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        List<QuizDTO> quizDTOs = new ArrayList<>();
        List<Quiz> quizzes = quizRepository.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            quizDTOs.add(QuizMapper.toDTO(quiz));
        }
        return quizDTOs;
    }

    @Override
    public boolean insertQuiz(QuizDTO quizDTO) {
        Quiz quiz = QuizMapper.toModel(quizDTO);
        return quizRepository.insertQuiz(quiz);
    }

    @Override
    public boolean updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = QuizMapper.toModel(quizDTO);
        return quizRepository.updateQuiz(quiz);
    }

    @Override
    public boolean deleteQuiz(int quizId) {
        if(questionQuizRepository.deleteAllQuestionsForQuiz(quizId) < 0) { return  false; }
        return quizRepository.deleteQuiz(quizId);
    }

    public int getLastQuizId() {
        List<QuizDTO> quizzes = QuizMapper.toDTO(quizRepository.getAllQuizzes());
        if (quizzes.isEmpty()) {
            return -1;
        }
        return quizzes.get(quizzes.size() - 1).getId();
    }
}