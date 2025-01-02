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
        try (Cursor cursor = queryQuizById(quizId)) {
            if (cursor.moveToFirst()) {
                return mapCursorToQuiz(cursor);
            }
        }
        return null;
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        try (Cursor cursor = queryAllQuizzes()) {
            while (cursor.moveToNext()) {
                quizzes.add(mapCursorToQuiz(cursor));
            }
        }
        return quizzes;
    }

    @Override
    public boolean insertQuiz(Quiz quiz) {
        return dbHelper.getWritableDatabase().insert(TABLE_QUIZ, null, mapQuizToContentValues(quiz)) != -1;
    }

    @Override
    public boolean updateQuiz(Quiz quiz) {
        return dbHelper.getWritableDatabase().update(
                TABLE_QUIZ,
                mapQuizToContentValues(quiz),
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(quiz.getId())}
        ) > 0;
    }

    @Override
    public boolean deleteQuiz(int quizId) {
        return dbHelper.getWritableDatabase().delete(
                TABLE_QUIZ,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(quizId)}
        ) > 0;
    }

    private Cursor queryQuizById(int quizId) {
        return dbHelper.getReadableDatabase().query(
                TABLE_QUIZ,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(quizId)},
                null,
                null,
                null
        );
    }

    private Cursor queryAllQuizzes() {
        return dbHelper.getReadableDatabase().query(
                TABLE_QUIZ,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Quiz mapCursorToQuiz(Cursor cursor) {
        return new Quiz(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        );
    }

    private ContentValues mapQuizToContentValues(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_NAME, quiz.getQuizName());
        values.put(COLUMN_DATE, quiz.getDate());
        return values;
    }
}