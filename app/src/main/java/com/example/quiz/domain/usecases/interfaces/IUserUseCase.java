package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.UserDTO;

import java.util.List;

public interface IUserUseCase {
    UserDTO getUserById(int userId);
    List<UserDTO> getAllUsers();
    boolean insertUser(UserDTO userDTO);
    boolean updateUser(UserDTO userDTO);
    boolean deleteUser(int userId);
    UserDTO login(String email, String password);
}
