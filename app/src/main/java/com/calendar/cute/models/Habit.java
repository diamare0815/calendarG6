package com.calendar.cute.models;

public class  Habit {
    private String name;
    private String icon;
    private int currentStreak;
    private int goalDays;
    private String color;
    private boolean[] completedDays;

    public Habit(String name, String icon, int currentStreak, int goalDays, String color) {
        this.name = name;
        this.icon = icon;
        this.currentStreak = currentStreak;
        this.goalDays = goalDays;
        this.color = color;
        this.completedDays = new boolean[30];
    }

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
    public boolean[] getCompletedDays() { return completedDays; }
    public void incrementStreak() { this.currentStreak++; }
    public int getProgress() { return (currentStreak * 100) / goalDays; }
}