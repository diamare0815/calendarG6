package com.calendar.cute.models;

public class Note {
    private String title;
    private String content;
    private String color;
    private long timestamp;

    public Note(String title, String content, String color, long timestamp) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.timestamp = timestamp;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}