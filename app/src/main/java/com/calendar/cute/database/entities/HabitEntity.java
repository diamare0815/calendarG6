package com.calendar.cute.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String icon;
    public int currentStreak;
    public int goalDays;
    public int progress;
    public String color;
    public String startDate;
    public String completedDates;

    public HabitEntity(String name, String icon, int currentStreak, int goalDays, int progress, String color, String startDate, String completedDates) {
        this.name = name;
        this.icon = icon;
        this.currentStreak = currentStreak;
        this.goalDays = goalDays;
        this.progress = progress;
        this.color = color;
        this.startDate = startDate;
        this.completedDates = completedDates;
    }
}