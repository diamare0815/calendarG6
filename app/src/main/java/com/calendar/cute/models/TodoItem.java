package com.calendar.cute.models;

import java.util.ArrayList;
import java.util.List;

public class TodoItem {
    private String title;
    private String category;
    private boolean completed;
    private boolean important;
    private String color;
    private long dueDate;
    private List<TodoItem> subtasks;

    public TodoItem(String title, String category, boolean completed, boolean important, String color) {
        this.title = title;
        this.category = category;
        this.completed = completed;
        this.important = important;
        this.color = color;
        this.subtasks = new ArrayList<>();
    }

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
    public List<TodoItem> getSubtasks() { return subtasks; }
    public void addSubtask(TodoItem subtask) { this.subtasks.add(subtask); }
}