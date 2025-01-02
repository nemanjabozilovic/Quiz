package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.UserQuiz;
import com.example.quiz.domain.mappers.UserQuizMapper;
import com.example.quiz.domain.models.UserQuizDTO;
import com.example.quiz.domain.repositories.IUserQuizRepository;
import com.example.quiz.domain.usecases.interfaces.IUserQuizUseCase;

import java.util.ArrayList;
import java.util.List;

public class UserQuizUseCase implements IUserQuizUseCase {
    private final IUserQuizRepository userQuizRepository;

    public UserQuizUseCase(IUserQuizRepository userQuizRepository) {
        this.userQuizRepository = userQuizRepository;
    }

    @Override
    public List<UserQuizDTO> getUsersForQuiz(int quizId) {
        List<UserQuiz> userQuizzes = userQuizRepository.getUsersForQuiz(quizId);
        List<UserQuizDTO> userQuizDTOs = new ArrayList<>();
        for (UserQuiz userQuiz : userQuizzes) {
            userQuizDTOs.add(UserQuizMapper.toDTO(userQuiz));
        }
        return userQuizDTOs;
    }

    @Override
    public List<UserQuizDTO> getQuizzesForUser(int userId) {
        List<UserQuiz> userQuizzes = userQuizRepository.getQuizzesForUser(userId);
        List<UserQuizDTO> userQuizDTOs = new ArrayList<>();
        for (UserQuiz userQuiz : userQuizzes) {
            userQuizDTOs.add(UserQuizMapper.toDTO(userQuiz));
        }
        return userQuizDTOs;
    }

    @Override
    public long insertUserQuiz(UserQuizDTO userQuizDTO) {
        UserQuiz userQuiz = UserQuizMapper.toModel(userQuizDTO);
        return userQuizRepository.insertUserQuiz(userQuiz);
    }

    @Override
    public long deleteUserQuiz(int userId, int quizId) {
        return userQuizRepository.deleteUserQuiz(userId, quizId);
    }

    @Override
    public List<UserQuizDTO> getAllUserQuizzes() {
        List<UserQuiz> userQuizzes = userQuizRepository.getAllUserQuizzes();
        List<UserQuizDTO> userQuizDTOs = new ArrayList<>();
        for (UserQuiz userQuiz : userQuizzes) {
            userQuizDTOs.add(UserQuizMapper.toDTO(userQuiz));
        }

        return userQuizDTOs;
    }
}