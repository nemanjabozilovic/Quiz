package com.example.quiz.domain.repositories;

import com.example.quiz.data.models.Role;

import java.util.List;

public interface IRoleRepository {
    Role getRoleById(int roleId);
    List<Role> getAllRoles();
    boolean insertRole(Role role);
    boolean updateRole(Role role);
    boolean deleteRole(int roleId);
}
