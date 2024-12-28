package com.example.quiz.data.models;

public class QuestionQuiz {
    private int questionId;
    private int quizId;

    public QuestionQuiz() {}

    public QuestionQuiz(int questionId, int quizId) {
        this.questionId = questionId;
        this.quizId = quizId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
}
