package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.Question;
import com.example.quiz.domain.mappers.QuestionMapper;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.repositories.IQuestionRepository;
import com.example.quiz.domain.usecases.interfaces.IQuestionUseCase;

import java.util.ArrayList;
import java.util.List;

public class QuestionUseCase implements IQuestionUseCase {
    private final IQuestionRepository questionRepository;

    public QuestionUseCase(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionDTO getQuestionById(int questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question != null) {
            return QuestionMapper.toDTO(question);
        }
        return null;
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<Question> questions = questionRepository.getAllQuestions();
        for (Question question : questions) {
            questionDTOs.add(QuestionMapper.toDTO(question));
        }
        return questionDTOs;
    }

    @Override
    public boolean insertQuestion(QuestionDTO questionDTO) {
        Question question = QuestionMapper.toModel(questionDTO);
        return questionRepository.insertQuestion(question);
    }

    @Override
    public boolean updateQuestion(QuestionDTO questionDTO) {
        Question question = QuestionMapper.toModel(questionDTO);
        return questionRepository.updateQuestion(question);
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        return questionRepository.deleteQuestion(questionId);
    }
}