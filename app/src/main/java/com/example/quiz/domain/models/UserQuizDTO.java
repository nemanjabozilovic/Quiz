package com.example.quiz.domain.models;

public class UserQuizDTO {
    private int userId;
    private int quizId;
    private int totalNumberOfPoints;

    public UserQuizDTO() {}

    public UserQuizDTO(int userId, int quizId, int totalNumberOfPoints) {
        this.userId = userId;
        this.quizId = quizId;
        this.totalNumberOfPoints = totalNumberOfPoints;
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

    public int getTotalNumberOfPoints() {
        return totalNumberOfPoints;
    }

    public void setTotalNumberOfPoints(int totalNumberOfPoints) {
        this.totalNumberOfPoints = totalNumberOfPoints;
    }
}