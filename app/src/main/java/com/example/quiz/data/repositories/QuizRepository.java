package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.data.models.Quiz;
import com.example.quiz.domain.repositories.IQuizRepository;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository implements IQuizRepository {
    private static final String TABLE_QUIZ = "quiz";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOTAL_NUMBER_OF_POINTS = "total_number_of_points";
    private static final String COLUMN_DATE = "date";

    private final SQLiteDatabase database;

    public QuizRepository(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public Quiz getQuizById(int quizId) {
        Cursor cursor = database.query(TABLE_QUIZ, null, COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Quiz quiz = new Quiz(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_NUMBER_OF_POINTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            );
            cursor.close();
            return quiz;
        }
        cursor.close();
        return null;
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = database.query(TABLE_QUIZ, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            quizzes.add(new Quiz(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_NUMBER_OF_POINTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            ));
        }
        cursor.close();
        return quizzes;
    }

    @Override
    public boolean insertQuiz(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_NUMBER_OF_POINTS, quiz.getTotalNumberOfPoints());
        values.put(COLUMN_DATE, quiz.getDate());
        long result = database.insert(TABLE_QUIZ, null, values);
        return result != -1;
    }

    @Override
    public boolean updateQuiz(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_NUMBER_OF_POINTS, quiz.getTotalNumberOfPoints());
        values.put(COLUMN_DATE, quiz.getDate());
        int rowsAffected = database.update(TABLE_QUIZ, values, COLUMN_ID + " = ?", new String[]{String.valueOf(quiz.getId())});
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteQuiz(int quizId) {
        int rowsAffected = database.delete(TABLE_QUIZ, COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)});
        return rowsAffected > 0;
    }
}