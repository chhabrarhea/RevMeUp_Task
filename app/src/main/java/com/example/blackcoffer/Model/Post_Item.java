package com.example.blackcoffer.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post_Item implements Parcelable {
    private boolean quiz;
    private ArrayList<Quiz_Item> questions;
    private String description;
    private Image_Item image;
    private String username;
    private String uid;
    Post_Item(){}

     Post_Item(String description,ArrayList<Quiz_Item> questions,boolean quiz,String uid,String username) {
        this.quiz = quiz;
        this.questions = questions;
        this.description = description;
        this.username = username;

        this.uid = uid;
    }
    Post_Item(String description,Image_Item image,boolean quiz,String uid,String username) {
        this.quiz = quiz;
        this.image = image;
        this.description = description;
        this.username = username;

        this.uid = uid;
    }
    public Post_Item(String uid, String description, ArrayList<Quiz_Item> questions, String username) {
        this.questions = questions;
        this.uid=uid;
        this.description = description;
        this.username = username;

        this.quiz=true;
        this.image=null;
    }

    public Post_Item(String uid,String description, Image_Item image, String username) {
        this.description = description;
        this.image = image;
        this.uid=uid;
        this.username = username;
        this.questions=null;
        this.quiz=false;
    }

    protected Post_Item(Parcel in) {
        quiz = in.readByte() != 0;
        description = in.readString();
        username = in.readString();
        uid = in.readString();
    }

    public static final Creator<Post_Item> CREATOR = new Creator<Post_Item>() {
        @Override
        public Post_Item createFromParcel(Parcel in) {
            return new Post_Item(in);
        }

        @Override
        public Post_Item[] newArray(int size) {
            return new Post_Item[size];
        }
    };

    public boolean isQuiz() {
        return this.quiz;
    }

    public void setQuiz(boolean quiz) {
        this.quiz = quiz;
    }

    public ArrayList<Quiz_Item> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Quiz_Item> questions) {
        this.questions = questions;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image_Item getImage() {
        return image;
    }

    public void setImage(Image_Item image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (quiz ? 1 : 0));
        parcel.writeString(description);
        parcel.writeString(username);
        parcel.writeString(uid);
    }
}
