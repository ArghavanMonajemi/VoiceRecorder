package com.android.voicerecorder.model;

public class Record {
    int id;
    String name;
    String date;
    String duration;
    boolean like;


    public boolean isLike() {return like;}

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }
}
