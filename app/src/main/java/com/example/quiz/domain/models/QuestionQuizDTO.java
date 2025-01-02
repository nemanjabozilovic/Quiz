package com.example.quiz.domain.models;

public class QuestionQuizDTO {
    private int questionId;
    private int quizId;

    public QuestionQuizDTO() {}

    public QuestionQuizDTO(int questionId, int quizId) {
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

    public static QuestionQuizDTO fromQuestionDTO(QuestionDTO questionDTO) {
        if (questionDTO == null) {
            return null;
        }
        return new QuestionQuizDTO(questionDTO.getId(), 0);
    }
}