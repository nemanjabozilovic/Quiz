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
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_QUESTION_QUIZ + " WHERE " + COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});
        while (cursor.moveToNext()) {
            questionsForQuiz.add(new QuestionQuiz(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ID)), quizId));
        }
        cursor.close();
        return questionsForQuiz;
    }

    @Override
    public List<QuestionQuiz> getQuizzesForQuestion(int questionId) {
        List<QuestionQuiz> quizzesForQuestion = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_QUESTION_QUIZ + " WHERE " + COLUMN_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)});
        while (cursor.moveToNext()) {
            quizzesForQuestion.add(new QuestionQuiz(questionId, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID))));
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
    public int deleteQuestionQuiz(int questionId, int quizId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(TABLE_QUESTION_QUIZ, COLUMN_QUESTION_ID + " = ? AND " + COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(questionId), String.valueOf(quizId)});
    }

    @Override
    public int getNumberOfQuestionsForQuiz(int quizId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTION_QUIZ + " WHERE " + COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});
        int count = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    @Override
    public int deleteAllQuestionsForQuiz(int quizId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(TABLE_QUESTION_QUIZ, COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});
    }

    @Override
    public boolean updateQuestionsForQuiz(int quizId, List<QuestionQuiz> newQuestions) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_QUESTION_QUIZ, COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});
            ContentValues values = new ContentValues();
            for (QuestionQuiz questionQuiz : newQuestions) {
                values.put(COLUMN_QUESTION_ID, questionQuiz.getQuestionId());
                values.put(COLUMN_QUIZ_ID, quizId);
                if (database.insert(TABLE_QUESTION_QUIZ, null, values) == -1) {
                    return false;
                }
            }
            database.setTransactionSuccessful();
            return true;
        } finally {
            database.endTransaction();
        }
    }
}