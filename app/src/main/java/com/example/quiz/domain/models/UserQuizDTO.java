package com.example.quiz.domain.models;

public class UserQuizDTO {
    private int userId;
    private int quizId;

    public UserQuizDTO() {}

    public UserQuizDTO(int userId, int quizId) {
        this.userId = userId;
        this.quizId = quizId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
}