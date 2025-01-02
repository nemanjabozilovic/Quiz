package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.mappers.QuestionMapper;
import com.example.quiz.domain.mappers.QuestionQuizMapper;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;
import com.example.quiz.domain.repositories.IQuestionQuizRepository;
import com.example.quiz.domain.repositories.IQuestionRepository;
import com.example.quiz.domain.usecases.interfaces.IQuestionQuizUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionQuizUseCase implements IQuestionQuizUseCase {
    private final IQuestionQuizRepository questionQuizRepository;
    private final IQuestionRepository questionRepository;

    public QuestionQuizUseCase(IQuestionQuizRepository questionQuizRepository, IQuestionRepository questionRepository) {
        this.questionQuizRepository = questionQuizRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<QuestionDTO> getQuestionsForQuiz(int quizId) {
        List<QuestionQuiz> questionQuizzes = questionQuizRepository.getQuestionsForQuiz(quizId);
        List<QuestionDTO> questionsForQuizz = new ArrayList<QuestionDTO>();

        for (QuestionQuiz questionQuiz : questionQuizzes) {
            int questionId = questionQuiz.getQuestionId();
            QuestionDTO question = QuestionMapper.toDTO(questionRepository.getQuestionById(questionId));
            questionsForQuizz.add(question);
        }

        return questionsForQuizz;
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
    public int deleteQuestionQuiz(int questionId, int quizId) {
        return questionQuizRepository.deleteQuestionQuiz(questionId, quizId);
    }

    @Override
    public int getNumberOfQuestionsForQuiz(int quizId) {
        return questionQuizRepository.getNumberOfQuestionsForQuiz(quizId);
    }

    @Override
    public int deleteAllQuestionsForQuiz(int quizId) {
        return questionQuizRepository.deleteAllQuestionsForQuiz(quizId);
    }

    @Override
    public boolean updateQuestionsForQuiz(int quizId, List<QuestionQuiz> newQuestions) {
        return  questionQuizRepository.updateQuestionsForQuiz(quizId, newQuestions);
    }
}