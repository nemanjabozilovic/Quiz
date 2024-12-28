package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.domain.models.UserRolesDTO;
import com.example.quiz.domain.repositories.IRoleRepository;
import com.example.quiz.domain.repositories.IUserRolesRepository;
import com.example.quiz.domain.usecases.interfaces.IUserRolesUseCase;

import java.util.List;
import java.util.stream.Collectors;

public class UserRolesUseCase implements IUserRolesUseCase {
    private final IUserRolesRepository userRolesRepository;
    private final IRoleRepository roleRepository;

    public UserRolesUseCase(IUserRolesRepository userRolesRepository, IRoleRepository roleRepository) {
        this.userRolesRepository = userRolesRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean assignRoleToUser(int userId, int roleId) {
        return userRolesRepository.assignRoleToUser(userId, roleId);
    }

    @Override
    public boolean removeRoleFromUser(int userId, int roleId) {
        return userRolesRepository.removeRoleFromUser(userId, roleId);
    }

    @Override
    public List<UserRolesDTO> getRolesByUserId(int userId) {
        List<Integer> roleIds = userRolesRepository.getRolesByUserId(userId);
        return roleIds.stream()
                .map(roleId -> {
                    String roleName = roleRepository.getRoleById(roleId).getRoleName();
                    return new UserRolesDTO(userId, roleId, roleName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRolesDTO> getUsersByRoleId(int roleId) {
        List<Integer> userIds = userRolesRepository.getUsersByRoleId(roleId);
        String roleName = roleRepository.getRoleById(roleId).getRoleName();
        return userIds.stream()
                .map(userId -> new UserRolesDTO(userId, roleId, roleName))
                .collect(Collectors.toList());
    }
}
