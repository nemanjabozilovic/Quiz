package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Role;
import com.example.quiz.domain.models.RoleDTO;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleName());
    }

    public static Role toModel(RoleDTO roleDTO) {
        return new Role(roleDTO.getId(), roleDTO.getRoleName());
    }
}