package com.example.quiz.domain.usecases.implementation;

import com.example.quiz.data.models.Role;
import com.example.quiz.data.models.User;
import com.example.quiz.domain.mappers.RoleMapper;
import com.example.quiz.domain.mappers.UserMapper;
import com.example.quiz.domain.models.RoleDTO;
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
        if (user != null) {
            return UserMapper.toDTO(user, null);
        }
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<User> users = userRepository.getAllUsers();
        for (User user : users) {
            userDTOs.add(UserMapper.toDTO(user, null));
        }
        return userDTOs;
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        return userRepository.insertUser(user);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        return userRepository.updateUser(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }

    @Override
    public UserDTO login(String email, String password) {
        User user = userRepository.login(email, password);
        if (user != null) {
            List<Integer> roleIds = userRolesRepository.getRolesByUserId(user.getId());
            if (roleIds != null && !roleIds.isEmpty()) {
                Integer roleId = roleIds.get(0);
                Role role = roleRepository.getRoleById(roleId);
                if (role != null) {
                    return UserMapper.toDTO(user, role);
                }
            }
        }
        return null;
    }
}