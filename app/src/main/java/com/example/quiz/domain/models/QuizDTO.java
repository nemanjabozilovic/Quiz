package com.example.quiz.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizDTO implements Parcelable {
    private int id;
    private String quizName;
    private String date;

    public QuizDTO() {}

    public QuizDTO(int id, String quizName, String date) {
        this.id = id;
        this.quizName = quizName;
        this.date = date;
    }

    protected QuizDTO(Parcel in) {
        id = in.readInt();
        quizName = in.readString();
        date = in.readString();
    }

    public static final Creator<QuizDTO> CREATOR = new Creator<QuizDTO>() {
        @Override
        public QuizDTO createFromParcel(Parcel in) {
            return new QuizDTO(in);
        }

        @Override
        public QuizDTO[] newArray(int size) {
            return new QuizDTO[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(quizName);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}