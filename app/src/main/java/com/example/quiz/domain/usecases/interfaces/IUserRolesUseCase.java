package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.UserRolesDTO;

import java.util.List;

public interface IUserRolesUseCase {
    boolean assignRoleToUser(int userId, int roleId);
    boolean removeRoleFromUser(int userId, int roleId);
    List<UserRolesDTO> getRolesByUserId(int userId);
    List<UserRolesDTO> getUsersByRoleId(int roleId);
}