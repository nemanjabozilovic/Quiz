package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.Role;
import com.example.quiz.domain.repositories.IRoleRepository;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository implements IRoleRepository {
    private static final String TABLE_ROLES = "roles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ROLE_NAME = "role_name";

    private final DatabaseHelper dbHelper;

    public RoleRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Role getRoleById(int roleId) {
        try (Cursor cursor = queryRoleById(roleId)) {
            if (cursor.moveToFirst()) {
                return mapCursorToRole(cursor);
            }
        }
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try (Cursor cursor = queryAllRoles()) {
            while (cursor.moveToNext()) {
                roles.add(mapCursorToRole(cursor));
            }
        }
        return roles;
    }

    @Override
    public boolean insertRole(Role role) {
        return dbHelper.getWritableDatabase().insert(TABLE_ROLES, null, mapRoleToContentValues(role)) != -1;
    }

    @Override
    public boolean updateRole(Role role) {
        return dbHelper.getWritableDatabase().update(
                TABLE_ROLES,
                mapRoleToContentValues(role),
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(role.getId())}
        ) > 0;
    }

    @Override
    public boolean deleteRole(int roleId) {
        return dbHelper.getWritableDatabase().delete(
                TABLE_ROLES,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(roleId)}
        ) > 0;
    }

    private Cursor queryRoleById(int roleId) {
        return dbHelper.getReadableDatabase().query(
                TABLE_ROLES,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(roleId)},
                null,
                null,
                null
        );
    }

    private Cursor queryAllRoles() {
        return dbHelper.getReadableDatabase().query(
                TABLE_ROLES,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Role mapCursorToRole(Cursor cursor) {
        return new Role(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE_NAME))
        );
    }

    private ContentValues mapRoleToContentValues(Role role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROLE_NAME, role.getRoleName());
        return values;
    }
}