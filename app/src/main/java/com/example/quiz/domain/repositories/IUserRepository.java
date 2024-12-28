package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.User;

import java.util.List;

public interface IUserRepository {
    User getUserById(int userId);
    List<User> getAllUsers();
    boolean insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    User login(String email, String password);
}
