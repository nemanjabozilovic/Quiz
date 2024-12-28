package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.data.models.Role;
import com.example.quiz.domain.repositories.IRoleRepository;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository implements IRoleRepository {
    private static final String TABLE_ROLES = "roles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ROLE_NAME = "role_name";

    private final SQLiteDatabase database;

    public RoleRepository(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public Role getRoleById(int roleId) {
        Cursor cursor = database.query(TABLE_ROLES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(roleId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Role role = new Role(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE_NAME))
            );
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        Cursor cursor = database.query(TABLE_ROLES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            roles.add(new Role(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE_NAME))
            ));
        }
        cursor.close();
        return roles;
    }

    @Override
    public boolean insertRole(Role role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROLE_NAME, role.getRoleName());
        long result = database.insert(TABLE_ROLES, null, values);
        return result != -1;
    }

    @Override
    public boolean updateRole(Role role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROLE_NAME, role.getRoleName());
        int rowsAffected = database.update(TABLE_ROLES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(role.getId())});
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteRole(int roleId) {
        int rowsAffected = database.delete(TABLE_ROLES, COLUMN_ID + " = ?", new String[]{String.valueOf(roleId)});
        return rowsAffected > 0;
    }
}