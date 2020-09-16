package com.example.blackcoffer.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz_Item implements Parcelable {
    private String question;
    private boolean answer;

    public Quiz_Item(boolean answer,String question) {
        this.question = question;
        this.answer = answer;

    }

    protected Quiz_Item(Parcel in) {
        question = in.readString();
        answer = in.readByte() != 0;

    }
    Quiz_Item(){}

    public static final Creator<Quiz_Item> CREATOR = new Creator<Quiz_Item>() {
        @Override
        public Quiz_Item createFromParcel(Parcel in) {
            return new Quiz_Item(in);
        }

        @Override
        public Quiz_Item[] newArray(int size) {
            return new Quiz_Item[size];
        }
    };


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeByte((byte) (answer ? 1 : 0));
    }
}
