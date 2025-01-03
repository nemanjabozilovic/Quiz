package com.example.quiz.data.models;

public class UserQuiz {
    private int id;
    private int userId;
    private int quizId;
    private int totalNumberOfPoints;

    public UserQuiz() {}

    public UserQuiz(int id, int userId, int quizId, int totalNumberOfPoints) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.totalNumberOfPoints = totalNumberOfPoints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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