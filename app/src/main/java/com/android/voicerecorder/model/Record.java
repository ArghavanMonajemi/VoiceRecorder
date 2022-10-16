package com.android.voicerecorder.model;

public class Record {
    String id;
    String name;
    String date;
    boolean like;
    String path;

    public Record(String id, String name, String date, boolean like, String path) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.like = like;
        this.path = path;
    }

    public Record(String name, String date, String path) {
        this.name = name;
        this.date = date;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public boolean isLike() {return like;}

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
