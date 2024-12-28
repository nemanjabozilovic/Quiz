package com.example.quiz.domain.mappers;

import com.example.quiz.data.models.UserRoles;
import com.example.quiz.domain.models.UserRolesDTO;

public class UserRolesMapper {
    public static UserRolesDTO toDTO(UserRoles userRoles, String roleName) {
        return new UserRolesDTO(
                userRoles.getUserId(),
                userRoles.getRoleId(),
                roleName
        );
    }

    public static UserRoles toModel(UserRolesDTO userRolesDTO) {
        return new UserRoles(
                userRolesDTO.getUserId(),
                userRolesDTO.getRoleId()
        );
    }
}
