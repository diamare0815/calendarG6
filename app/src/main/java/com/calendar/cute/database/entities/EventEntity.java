
package com.calendar.cute.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
@Entity(tableName = "events")
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String time;
    private String date;
    private String color;
    private boolean hasReminder;
    private long timestamp;

    public EventEntity(String title, String time, String date, String color, boolean hasReminder, long timestamp) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.color = color;
        this.hasReminder = hasReminder;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isHasReminder() { return hasReminder; }
    public void setHasReminder(boolean hasReminder) { this.hasReminder = hasReminder; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
