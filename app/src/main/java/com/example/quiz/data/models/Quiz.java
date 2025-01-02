package com.example.quiz.data.models;

public class Quiz {
    private int id;
    private String quizName;
    private String date;

    public Quiz() {}

    public Quiz(int id, String quizName, String date) {
        this.id = id;
        this.quizName = quizName;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }
}