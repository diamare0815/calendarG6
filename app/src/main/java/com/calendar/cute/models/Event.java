package com.calendar.cute.models;

import java.io.Serializable;
import java.util.Objects;

public class Event implements Serializable {

    private String title;
    private String timeStart;
    private String timeEnd;
    private String date;
    private String color;

    private boolean hasReminder = false;
    private int reminderMinutes = 0;

    public Event(String title, String timeStart, String timeEnd, String date, String color){
        this.title = title;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.date = date;
        this.color = color;
    }

    // getter/setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTimeStart() { return timeStart; }
    public void setTimeStart(String timeStart) { this.timeStart = timeStart; }

    public String getTimeEnd() { return timeEnd; }
    public void setTimeEnd(String timeEnd) { this.timeEnd = timeEnd; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public boolean hasReminder() { return hasReminder; }
    public void setHasReminder(boolean hasReminder) { this.hasReminder = hasReminder; }

    public int getReminderMinutes() { return reminderMinutes; }
    public void setReminderMinutes(int reminderMinutes) { this.reminderMinutes = reminderMinutes; }

    // Helpful overrides for stable identity (useful for alarm requestCode generation, comparisons...)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;
        return Objects.equals(title, event.title) &&
                Objects.equals(timeStart, event.timeStart) &&
                Objects.equals(timeEnd, event.timeEnd) &&
                Objects.equals(date, event.date) &&
                Objects.equals(color, event.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, timeStart, timeEnd, date, color);
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", date='" + date + '\'' +
                ", color='" + color + '\'' +
                ", hasReminder=" + hasReminder +
                ", reminderMinutes=" + reminderMinutes +
                '}';
    }
}
