package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.User;
import com.example.quiz.domain.repositories.IUserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private final DatabaseHelper dbHelper;

    public UserRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public User getUserById(int userId) {
        Cursor cursor = null;
        try {
            cursor = queryUserById(userId);
            if (cursor.moveToFirst()) {
                return mapCursorToUser(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = queryAllUsers();
            while (cursor != null && cursor.moveToNext()) {
                users.add(mapCursorToUser(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return users;
    }

    @Override
    public User insertUser(User user) {
        ContentValues values = mapUserToContentValues(user);
        long result = dbHelper.getWritableDatabase().insert(TABLE_USERS, null, values);

        if (result != -1) {
            user.setId((int) result);
            return user;
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        ContentValues values = mapUserToContentValues(user);
        int rowsAffected = dbHelper.getWritableDatabase().update(
                TABLE_USERS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())}
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteUser(int userId) {
        int rowsAffected = dbHelper.getWritableDatabase().delete(
                TABLE_USERS,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );
        return rowsAffected > 0;
    }

    @Override
    public User login(String email, String password) {
        Cursor cursor = null;
        try {
            cursor = queryUserByEmailAndPassword(email, password);
            if (cursor.moveToFirst()) {
                return mapCursorToUser(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private Cursor queryUserById(int userId) {
        return dbHelper.getReadableDatabase().query(
                TABLE_USERS,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
    }

    private Cursor queryAllUsers() {
        return dbHelper.getReadableDatabase().query(
                TABLE_USERS,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor queryUserByEmailAndPassword(String email, String password) {
        return dbHelper.getReadableDatabase().query(
                TABLE_USERS,
                null,
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password},
                null,
                null,
                null
        );
    }

    private User mapCursorToUser(Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
        );
    }

    private ContentValues mapUserToContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        return values;
    }
}