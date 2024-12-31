package com.example.quiz.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionDTO implements Parcelable {
    private int id;
    private String text;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String correctAnswer;
    private int numberOfPoints;

    public QuestionDTO() {}

    public QuestionDTO(int id, String text, String option1, String option2, String option3, String option4, String option5, String correctAnswer, int numberOfPoints) {
        this.id = id;
        this.text = text;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.correctAnswer = correctAnswer;
        this.numberOfPoints = numberOfPoints;
    }

    protected QuestionDTO(Parcel in) {
        id = in.readInt();
        text = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        option5 = in.readString();
        correctAnswer = in.readString();
        numberOfPoints = in.readInt();
    }

    public static final Creator<QuestionDTO> CREATOR = new Creator<QuestionDTO>() {
        @Override
        public QuestionDTO createFromParcel(Parcel in) {
            return new QuestionDTO(in);
        }

        @Override
        public QuestionDTO[] newArray(int size) {
            return new QuestionDTO[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeString(option4);
        dest.writeString(option5);
        dest.writeString(correctAnswer);
        dest.writeInt(numberOfPoints);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }
}