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
        try (Cursor cursor = queryUserById(userId)) {
            return cursor != null && cursor.moveToFirst() ? mapCursorToUser(cursor) : null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Cursor cursor = queryAllUsers()) {
            while (cursor != null && cursor.moveToNext()) {
                users.add(mapCursorToUser(cursor));
            }
        }
        return users;
    }

    @Override
    public User insertUser(User user) {
        long result = dbHelper.getWritableDatabase().insert(TABLE_USERS, null, mapUserToContentValues(user));
        if (result != -1) {
            user.setId((int) result);
            return user;
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        return dbHelper.getWritableDatabase().update(
                TABLE_USERS,
                mapUserToContentValues(user),
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())}
        ) > 0;
    }

    @Override
    public boolean deleteUser(int userId) {
        return dbHelper.getWritableDatabase().delete(
                TABLE_USERS,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)}
        ) > 0;
    }

    @Override
    public User login(String email, String password) {
        try (Cursor cursor = queryUserByEmailAndPassword(email, password)) {
            return cursor != null && cursor.moveToFirst() ? mapCursorToUser(cursor) : null;
        }
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