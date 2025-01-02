package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.Quiz;
import com.example.quiz.domain.repositories.IQuizRepository;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository implements IQuizRepository {
    private static final String TABLE_QUIZ = "quiz";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUIZ_NAME = "quiz_name";
    private static final String COLUMN_DATE = "date";

    private final DatabaseHelper dbHelper;

    public QuizRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Quiz getQuizById(int quizId) {
        Cursor cursor = null;
        try {
            cursor = queryQuizById(quizId);
            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToQuiz(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = queryAllQuizzes();
            while (cursor != null && cursor.moveToNext()) {
                quizzes.add(mapCursorToQuiz(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return quizzes;
    }

    @Override
    public boolean insertQuiz(Quiz quiz) {
        ContentValues values = mapQuizToContentValues(quiz);
        long result = dbHelper.getWritableDatabase().insert(TABLE_QUIZ, null, values);
        return result != -1;
    }

    @Override
    public boolean updateQuiz(Quiz quiz) {
        ContentValues values = mapQuizToContentValues(quiz);
        int rowsAffected = dbHelper.getWritableDatabase().update(TABLE_QUIZ, values, COLUMN_ID + " = ?", new String[]{String.valueOf(quiz.getId())});
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteQuiz(int quizId) {
        int rowsAffected = dbHelper.getWritableDatabase().delete(TABLE_QUIZ, COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)});
        return rowsAffected > 0;
    }

    private Cursor queryQuizById(int quizId) {
        return dbHelper.getReadableDatabase().query(TABLE_QUIZ, null, COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)}, null, null, null);
    }

    private Cursor queryAllQuizzes() {
        return dbHelper.getReadableDatabase().query(TABLE_QUIZ, null, null, null, null, null, null);
    }

    private Quiz mapCursorToQuiz(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
        String quizName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_NAME));
        return new Quiz(id, quizName, date);
    }

    private ContentValues mapQuizToContentValues(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_NAME, quiz.getQuizName());
        values.put(COLUMN_DATE, quiz.getDate());
        return values;
    }
}