package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.User;
import com.example.quiz.domain.mappers.UserMapper;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.repositories.IUserRepository;
import com.example.quiz.domain.usecases.interfaces.IUserUseCase;

import java.util.ArrayList;
import java.util.List;

public class UserUseCase implements IUserUseCase {
    private final IUserRepository userRepository;

    public UserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(int userId) {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return UserMapper.toDTO(user, null);
        }
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<User> users = userRepository.getAllUsers();
        for (User user : users) {
            userDTOs.add(UserMapper.toDTO(user, null));
        }
        return userDTOs;
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        return userRepository.insertUser(user);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        return userRepository.updateUser(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }

    @Override
    public UserDTO login(String email, String password) {
        User user = userRepository.login(email, password);
        if (user != null) {
            return UserMapper.toDTO(user, null);
        }
        return null;
    }
}