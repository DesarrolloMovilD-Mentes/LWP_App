package com.example.lwp_lab01.models;

public class PDF {

    private String postKey;
    private String title;
    private String description;
    private Object timeStamp ;


    public PDF(String title, String description, String picture, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
//      this.timeStamp = ServerValue.TIMESTAMP;
    }

    // make sure to have an empty constructor inside ur model class
    public PDF() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}

