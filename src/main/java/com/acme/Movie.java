package com.acme;

public class Movie {
    String tag;
    String title;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "com.acme.Movie{" +
                "tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
