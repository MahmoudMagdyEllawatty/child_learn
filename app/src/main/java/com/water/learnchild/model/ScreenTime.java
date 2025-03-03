package com.water.learnchild.model;

public class ScreenTime {
    private String key;
    private String from_hour;
    private String to_hour;
    private Child child;

    public ScreenTime() {
    }

    public ScreenTime(String key, String from_hour, String to_hour, Child child) {
        this.key = key;
        this.from_hour = from_hour;
        this.to_hour = to_hour;
        this.child = child;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom_hour() {
        return from_hour;
    }

    public void setFrom_hour(String from_hour) {
        this.from_hour = from_hour;
    }

    public String getTo_hour() {
        return to_hour;
    }

    public void setTo_hour(String to_hour) {
        this.to_hour = to_hour;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
