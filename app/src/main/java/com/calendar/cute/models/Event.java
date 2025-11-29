package com.calendar.cute.models;

public class Event {
    private String title;
    private String time;
    private String date;
    private String color;
    private boolean hasReminder;

    public Event(String title, String time, String date, String color) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.color = color;
        this.hasReminder = false;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean hasReminder() { return hasReminder; }
    public void setHasReminder(boolean hasReminder) { this.hasReminder = hasReminder; }
}