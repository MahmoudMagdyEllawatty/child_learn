package com.water.learnchild.utils;

public class LetterHelper {
    private int source;
    private String url;
    private String correctPronounce;

    public LetterHelper(int source, String url, String correctPronounce) {
        this.source = source;
        this.url = url;
        this.correctPronounce = correctPronounce;
    }


    public String getCorrectPronounce() {
        return correctPronounce;
    }

    public void setCorrectPronounce(String correctPronounce) {
        this.correctPronounce = correctPronounce;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

