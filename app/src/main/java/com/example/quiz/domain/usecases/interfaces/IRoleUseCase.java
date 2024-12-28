package com.example.quiz.domain.usecases.interfaces;

import com.example.quiz.domain.models.RoleDTO;

import java.util.List;

public interface IRoleUseCase {
    RoleDTO getRoleById(int roleId);
    List<RoleDTO> getAllRoles();
    boolean insertRole(RoleDTO roleDTO);
    boolean updateRole(RoleDTO roleDTO);
    boolean deleteRole(int roleId);
}
