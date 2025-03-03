package com.water.learnchild.model;

public class Goal {
    private String key;
    private String title;
    private int state;
    private Child child;

    public Goal() {
    }

    public Goal(String key, String title, int state, Child child) {
        this.key = key;
        this.title = title;
        this.state = state;
        this.child = child;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
