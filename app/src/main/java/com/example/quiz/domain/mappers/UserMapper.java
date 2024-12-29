package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.Role;
import com.example.quiz.data.models.User;
import com.example.quiz.domain.models.RoleDTO;
import com.example.quiz.domain.models.UserDTO;

public class UserMapper {
    public static UserDTO toDTO(User user, Role role) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                new RoleDTO(role.getId(), role.getRoleName())
        );
    }

    public static User toModel(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                null
        );
    }
}