package com.example.quiz.domain.models;

public class UserQuizDTO {
    private int id;
    private int userId;
    private int quizId;
    private int totalNumberOfPoints;
    private String userName;
    private String quizName;

    public UserQuizDTO() {}

    public UserQuizDTO(int id, int userId, int quizId, int totalNumberOfPoints) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
}