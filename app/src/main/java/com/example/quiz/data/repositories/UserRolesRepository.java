package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.domain.repositories.IUserRolesRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRolesRepository implements IUserRolesRepository {
    private static final String TABLE_USER_ROLES = "user_roles";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_ROLE_ID = "role_id";

    private final SQLiteDatabase database;

    public UserRolesRepository(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public boolean assignRoleToUser(int userId, int roleId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ROLE_ID, roleId);
        long result = database.insert(TABLE_USER_ROLES, null, values);
        return result != -1;
    }

    @Override
    public boolean removeRoleFromUser(int userId, int roleId) {
        int rowsAffected = database.delete(
                TABLE_USER_ROLES,
                COLUMN_USER_ID + " = ? AND " + COLUMN_ROLE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(roleId)}
        );
        return rowsAffected > 0;
    }

    @Override
    public List<Integer> getRolesByUserId(int userId) {
        List<Integer> roleIds = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_USER_ROLES,
                new String[]{COLUMN_ROLE_ID},
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            roleIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE_ID)));
        }
        cursor.close();
        return roleIds;
    }

    @Override
    public List<Integer> getUsersByRoleId(int roleId) {
        List<Integer> userIds = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_USER_ROLES,
                new String[]{COLUMN_USER_ID},
                COLUMN_ROLE_ID + " = ?",
                new String[]{String.valueOf(roleId)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            userIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
        }
        cursor.close();
        return userIds;
    }
}