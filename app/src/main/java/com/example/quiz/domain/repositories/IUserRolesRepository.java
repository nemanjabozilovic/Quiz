package com.example.quiz.domain.repositories;

import java.util.List;

public interface IUserRolesRepository {
    boolean assignRoleToUser(int userId, int roleId);
    boolean removeRoleFromUser(int userId, int roleId);
    List<Integer> getRolesByUserId(int userId);
    List<Integer> getUsersByRoleId(int roleId);
}
