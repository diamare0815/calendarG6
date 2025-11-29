package com.calendar.cute.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String icon;
    private int currentStreak;
    private int goalDays;
    private String color;
    private long timestamp;

    public HabitEntity(String name, String icon, int currentStreak, int goalDays, String color, long timestamp) {
        this.name = name;
        this.icon = icon;
        this.currentStreak = currentStreak;
        this.goalDays = goalDays;
        this.color = color;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    public int getGoalDays() { return goalDays; }
    public void setGoalDays(int goalDays) { this.goalDays = goalDays; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}