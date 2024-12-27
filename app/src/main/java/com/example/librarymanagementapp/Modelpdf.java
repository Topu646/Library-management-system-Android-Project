package com.example.librarymanagementapp;

public class Modelpdf {

    String uid, id, title, description, catagoryid,url;
    long timestamp;

    public Modelpdf() {
    }

    public Modelpdf(String uid, String id, String title, String description, String catagoryid, String url, long timestamp) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.catagoryid = catagoryid;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatagoryid() {
        return catagoryid;
    }

    public void setCatagoryid(String catagoryid) {
        this.catagoryid = catagoryid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
