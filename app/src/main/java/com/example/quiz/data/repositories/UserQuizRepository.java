package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.data.models.UserQuiz;
import com.example.quiz.domain.repositories.IUserQuizRepository;

import java.util.ArrayList;
import java.util.List;

public class UserQuizRepository implements IUserQuizRepository {
    private static final String TABLE_USER_QUIZ = "user_quiz";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_QUIZ_ID = "quiz_id";

    private final SQLiteDatabase database;

    public UserQuizRepository(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public List<UserQuiz> getUsersForQuiz(int quizId) {
        List<UserQuiz> userQuizzes = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_USER_QUIZ,
                null,
                COLUMN_QUIZ_ID + " = ?",
                new String[]{String.valueOf(quizId)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            userQuizzes.add(new UserQuiz(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID))
            ));
        }
        cursor.close();
        return userQuizzes;
    }

    @Override
    public List<UserQuiz> getQuizzesForUser(int userId) {
        List<UserQuiz> userQuizzes = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_USER_QUIZ,
                null,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            userQuizzes.add(new UserQuiz(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID))
            ));
        }
        cursor.close();
        return userQuizzes;
    }

    @Override
    public void insertUserQuiz(UserQuiz userQuiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userQuiz.getUserId());
        values.put(COLUMN_QUIZ_ID, userQuiz.getQuizId());
        database.insert(TABLE_USER_QUIZ, null, values);
    }

    @Override
    public void deleteUserQuiz(int userId, int quizId) {
        database.delete(
                TABLE_USER_QUIZ,
                COLUMN_USER_ID + " = ? AND " + COLUMN_QUIZ_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(quizId)}
        );
    }
}