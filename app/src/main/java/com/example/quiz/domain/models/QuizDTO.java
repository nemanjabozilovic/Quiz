package com.example.quiz.domain.models;

public class QuizDTO {
    private int id;
    private int totalNumberOfPoints;
    private String date;

    public QuizDTO() {}

    public QuizDTO(int id, int totalNumberOfPoints, String date) {
        this.id = id;
        this.totalNumberOfPoints = totalNumberOfPoints;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalNumberOfPoints() {
        return totalNumberOfPoints;
    }

    public void setTotalNumberOfPoints(int totalNumberOfPoints) {
        this.totalNumberOfPoints = totalNumberOfPoints;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}