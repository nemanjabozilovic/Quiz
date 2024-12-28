package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.Role;
import com.example.quiz.domain.mappers.RoleMapper;
import com.example.quiz.domain.models.RoleDTO;
import com.example.quiz.domain.repositories.IRoleRepository;
import com.example.quiz.domain.usecases.interfaces.IRoleUseCase;

import java.util.ArrayList;
import java.util.List;

public class RoleUseCase implements IRoleUseCase {
    private final IRoleRepository roleRepository;

    public RoleUseCase(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO getRoleById(int roleId) {
        Role role = roleRepository.getRoleById(roleId);
        if (role != null) {
            return RoleMapper.toDTO(role);
        }
        return null;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roleDTOs = new ArrayList<>();
        List<Role> roles = roleRepository.getAllRoles();
        for (Role role : roles) {
            roleDTOs.add(RoleMapper.toDTO(role));
        }
        return roleDTOs;
    }

    @Override
    public boolean insertRole(RoleDTO roleDTO) {
        Role role = RoleMapper.toModel(roleDTO);
        return roleRepository.insertRole(role);
    }

    @Override
    public boolean updateRole(RoleDTO roleDTO) {
        Role role = RoleMapper.toModel(roleDTO);
        return roleRepository.updateRole(role);
    }

    @Override
    public boolean deleteRole(int roleId) {
        return roleRepository.deleteRole(roleId);
    }
}