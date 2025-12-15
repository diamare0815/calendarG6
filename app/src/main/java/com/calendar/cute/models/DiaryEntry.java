package com.calendar.cute.models;

public class DiaryEntry {
    private String title;
    private String content;
    private String mood;
    private String date;
    private long timestamp;

    public DiaryEntry(String title, String content, String mood, String date, long timestamp) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}