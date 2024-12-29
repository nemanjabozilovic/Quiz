package com.example.quiz.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.models.QuestionQuiz;
import com.example.quiz.domain.repositories.IQuestionQuizRepository;

import java.util.ArrayList;
import java.util.List;

public class QuestionQuizRepository implements IQuestionQuizRepository {
    private static final String TABLE_QUESTION_QUIZ = "question_quiz";
    private static final String COLUMN_QUESTION_ID = "question_id";
    private static final String COLUMN_QUIZ_ID = "quiz_id";

    private final DatabaseHelper dbHelper;

    public QuestionQuizRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<QuestionQuiz> getQuestionsForQuiz(int quizId) {
        List<QuestionQuiz> questionsForQuiz = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_QUESTION_QUIZ + " WHERE " + COLUMN_QUIZ_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(quizId)});

        if (cursor.moveToFirst()) {
            do {
                int questionId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ID));
                questionsForQuiz.add(new QuestionQuiz(questionId, quizId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionsForQuiz;
    }

    @Override
    public List<QuestionQuiz> getQuizzesForQuestion(int questionId) {
        List<QuestionQuiz> quizzesForQuestion = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_QUESTION_QUIZ + " WHERE " + COLUMN_QUESTION_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(questionId)});

        if (cursor.moveToFirst()) {
            do {
                int quizId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID));
                quizzesForQuestion.add(new QuestionQuiz(questionId, quizId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return quizzesForQuestion;
    }

    @Override
    public void insertQuestionQuiz(QuestionQuiz questionQuiz) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_ID, questionQuiz.getQuestionId());
        values.put(COLUMN_QUIZ_ID, questionQuiz.getQuizId());

        database.insert(TABLE_QUESTION_QUIZ, null, values);
    }

    @Override
    public void deleteQuestionQuiz(int questionId, int quizId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_QUESTION_ID + " = ? AND " + COLUMN_QUIZ_ID + " = ?";
        String[] whereArgs = {String.valueOf(questionId), String.valueOf(quizId)};

        database.delete(TABLE_QUESTION_QUIZ, whereClause, whereArgs);
    }
}