package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.Role;
import com.example.quiz.data.models.User;;
import com.example.quiz.domain.mappers.UserMapper;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.repositories.IRoleRepository;
import com.example.quiz.domain.repositories.IUserRepository;
import com.example.quiz.domain.repositories.IUserRolesRepository;
import com.example.quiz.domain.usecases.interfaces.IUserUseCase;

import java.util.ArrayList;
import java.util.List;

public class UserUseCase implements IUserUseCase {
    private final IUserRepository userRepository;
    private final IUserRolesRepository userRolesRepository;
    private final IRoleRepository roleRepository;

    public UserUseCase(IUserRepository userRepository, IUserRolesRepository userRolesRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO getUserById(int userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) return null;

        Role role = getUserRole(userId);
        return UserMapper.toDTO(user, role);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<User> users = userRepository.getAllUsers();

        for (User user : users) {
            Role role = getUserRole(user.getId());
            userDTOs.add(UserMapper.toDTO(user, role));
        }

        return userDTOs;
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        User userInserted = userRepository.insertUser(user);
        if (userInserted == null) { return false; }

        boolean roleAssigned = assignRoleToUser(userDTO.getRole().getId(), user.getId());
        if (!roleAssigned) {
            userRepository.deleteUser(user.getId());
        }

        return roleAssigned;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        boolean userUpdated = userRepository.updateUser(user);

        if (!userUpdated) return false;

        Role currentRole = getUserRole(user.getId());
        if (currentRole == null) return false;

        if (currentRole.getId() != userDTO.getRole().getId()) {
            return updateUserRole(user.getId(), currentRole.getId(), userDTO.getRole().getId());
        }

        return true;
    }

    private boolean updateUserRole(int userId, int currentRoleId, int newRoleId) {
        boolean roleRemoved = userRolesRepository.removeRoleFromUser(userId, currentRoleId);
        boolean roleAssigned = userRolesRepository.assignRoleToUser(userId, newRoleId);
        return roleRemoved && roleAssigned;
    }

    private boolean assignRoleToUser(int roleId, int userId) {
        return userRolesRepository.assignRoleToUser(userId, roleId);
    }

    @Override
    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }

    @Override
    public UserDTO login(String email, String password) {
        User user = userRepository.login(email, password);
        if (user == null) return null;

        Role role = getUserRole(user.getId());
        return (role != null) ? UserMapper.toDTO(user, role) : null;
    }

    private Role getUserRole(int userId) {
        List<Integer> roleIds = userRolesRepository.getRolesByUserId(userId);
        if (roleIds.isEmpty()) return null;

        Integer roleId = roleIds.get(0);
        return roleRepository.getRoleById(roleId);
    }
}