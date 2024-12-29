package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.Question;
import com.example.quiz.domain.repositories.IQuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository implements IQuestionRepository {
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_OPTION_1 = "option_1";
    private static final String COLUMN_OPTION_2 = "option_2";
    private static final String COLUMN_OPTION_3 = "option_3";
    private static final String COLUMN_OPTION_4 = "option_4";
    private static final String COLUMN_OPTION_5 = "option_5";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    private static final String COLUMN_NUMBER_OF_POINTS = "number_of_points";

    private final DatabaseHelper dbHelper;

    public QuestionRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Question getQuestionById(int questionId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_ID + " = ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(questionId)});
            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToQuestion(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_QUESTIONS;
            cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    questions.add(mapCursorToQuestion(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return questions;
    }

    @Override
    public boolean insertQuestion(Question question) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = mapQuestionToContentValues(question);
        long result = database.insert(TABLE_QUESTIONS, null, values);
        return result != -1;
    }

    @Override
    public boolean updateQuestion(Question question) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = mapQuestionToContentValues(question);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(question.getId())};

        int rowsUpdated = database.update(TABLE_QUESTIONS, values, whereClause, whereArgs);
        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(questionId)};

        int rowsDeleted = database.delete(TABLE_QUESTIONS, whereClause, whereArgs);
        return rowsDeleted > 0;
    }

    private Question mapCursorToQuestion(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT));
        String option1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_1));
        String option2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_2));
        String option3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_3));
        String option4 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_4));
        String option5 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_5));
        String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER));
        int numberOfPoints = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_OF_POINTS));

        return new Question(id, text, option1, option2, option3, option4, option5, correctAnswer, numberOfPoints);
    }

    private ContentValues mapQuestionToContentValues(Question question) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, question.getText());
        values.put(COLUMN_OPTION_1, question.getOption1());
        values.put(COLUMN_OPTION_2, question.getOption2());
        values.put(COLUMN_OPTION_3, question.getOption3());
        values.put(COLUMN_OPTION_4, question.getOption4());
        values.put(COLUMN_OPTION_5, question.getOption5());
        values.put(COLUMN_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(COLUMN_NUMBER_OF_POINTS, question.getNumberOfPoints());
        return values;
    }
}