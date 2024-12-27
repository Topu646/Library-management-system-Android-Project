package com.example.librarymanagementapp;

public class ModelCategory {
    String id, catagory, uid;
    long timestamp;


    public ModelCategory() {

    }

    public ModelCategory(String id, String catagory, String uid, long timestamp) {
        this.id = id;
        this.catagory = catagory;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
