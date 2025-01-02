package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.UserQuiz;
import com.example.quiz.domain.repositories.IUserQuizRepository;

import java.util.ArrayList;
import java.util.List;

public class UserQuizRepository implements IUserQuizRepository {
    private static final String TABLE_USER_QUIZ = "user_quiz";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_QUIZ_ID = "quiz_id";
    private static final String COLUMN_TOTAL_NUMBER_OF_POINTS = "total_number_of_points";

    private final DatabaseHelper dbHelper;

    public UserQuizRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<UserQuiz> getUsersForQuiz(int quizId) {
        List<UserQuiz> userQuizzes = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = queryUserQuizByQuizId(quizId);
            while (cursor != null && cursor.moveToNext()) {
                userQuizzes.add(mapCursorToUserQuiz(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userQuizzes;
    }

    @Override
    public List<UserQuiz> getQuizzesForUser(int userId) {
        List<UserQuiz> userQuizzes = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = queryUserQuizByUserId(userId);
            while (cursor != null && cursor.moveToNext()) {
                userQuizzes.add(mapCursorToUserQuiz(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userQuizzes;
    }

    @Override
    public void insertUserQuiz(UserQuiz userQuiz) {
        ContentValues values = mapUserQuizToContentValues(userQuiz);
        dbHelper.getWritableDatabase().insert(TABLE_USER_QUIZ, null, values);
    }

    @Override
    public void deleteUserQuiz(int userId, int quizId) {
        dbHelper.getWritableDatabase().delete(
                TABLE_USER_QUIZ,
                COLUMN_USER_ID + " = ? AND " + COLUMN_QUIZ_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(quizId)}
        );
    }

    private Cursor queryUserQuizByQuizId(int quizId) {
        return dbHelper.getReadableDatabase().query(
                TABLE_USER_QUIZ,
                null,
                COLUMN_QUIZ_ID + " = ?",
                new String[]{String.valueOf(quizId)},
                null,
                null,
                null
        );
    }

    private Cursor queryUserQuizByUserId(int userId) {
        return dbHelper.getReadableDatabase().query(
                TABLE_USER_QUIZ,
                null,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
    }

    private UserQuiz mapCursorToUserQuiz(Cursor cursor) {
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        int quizId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID));
        int totalPoints = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_NUMBER_OF_POINTS));
        return new UserQuiz(userId, quizId, totalPoints);
    }

    private ContentValues mapUserQuizToContentValues(UserQuiz userQuiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userQuiz.getUserId());
        values.put(COLUMN_QUIZ_ID, userQuiz.getQuizId());
        values.put(COLUMN_TOTAL_NUMBER_OF_POINTS, userQuiz.getTotalNumberOfPoints());
        return values;
    }
}