package com.example.blackcoffer.Model;

public class User {
    private String name;
    private String uid;


    public User(String name, String uid) {
        this.name = name;
        this.uid=uid;
    }


    User(){}





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
