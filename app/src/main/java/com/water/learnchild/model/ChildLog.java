package com.water.learnchild.model;

public class ChildLog {
    private String key;
    private String date;
    private String description;
    private Child  child;
    private String imageURL;

    public ChildLog() {
    }

    public ChildLog(String key, String date, String description, Child child,String imageURL) {
        this.key = key;
        this.date = date;
        this.description = description;
        this.child = child;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
