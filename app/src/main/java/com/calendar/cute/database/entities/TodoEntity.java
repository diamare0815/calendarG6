package com.calendar.cute.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
@Entity(tableName = "todos")
public class TodoEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String category;
    private boolean completed;
    private boolean important;
    private String color;
    private long dueDate;
    private long timestamp;

    public TodoEntity(String title, String category, boolean completed, boolean important, String color, long dueDate, long timestamp) {
        this.title = title;
        this.category = category;
        this.completed = completed;
        this.important = important;
        this.color = color;
        this.dueDate = dueDate;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public boolean isImportant() { return important; }
    public void setImportant(boolean important) { this.important = important; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

// ==================== NoteEntity ====================
