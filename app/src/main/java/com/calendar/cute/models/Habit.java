package com.calendar.cute.models;

import com.calendar.cute.database.entities.HabitEntity;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Habit {

    private int id;
    private String name;
    private String icon;
    private int currentStreak;
    private int goalDays;
    private String color;
    private String startDate;
    private final Set<String> completedDates = new HashSet<>();

    public Habit(HabitEntity entity) {
        this.id = entity.id;
        this.name = entity.name;
        this.icon = entity.icon;
        this.currentStreak = entity.currentStreak;
        this.goalDays = entity.goalDays;
        this.color = entity.color;
        this.startDate = entity.startDate;

        if (entity.completedDates != null && !entity.completedDates.trim().isEmpty()) {
            String[] dates = entity.completedDates.split(",");
            this.completedDates.addAll(Arrays.asList(dates));
        }
    }

    public Habit(String name, String icon, int currentStreak, int goalDays, String color, String startDate) {
        this.name = name;
        this.icon = icon;
        this.currentStreak = currentStreak;
        this.goalDays = goalDays;
        this.color = color;
        this.startDate = startDate;
    }

    public HabitEntity toEntity() {
        String datesStr = String.join(",", completedDates);
        int currentProgress = getProgress();

        HabitEntity entity = new HabitEntity(
                this.name,
                this.icon,
                this.currentStreak,
                this.goalDays,
                currentProgress,
                this.color,
                this.startDate,
                datesStr
        );
        entity.id = this.id;
        return entity;
    }

    public void toggleToday() {
        String today = getTodayDate();
        if (completedDates.contains(today)) {
            completedDates.remove(today);
            decrementStreak();
        } else {
            completedDates.add(today);
            incrementStreak();
        }
    }

    public boolean isCompletedToday() {
        return completedDates.contains(getTodayDate());
    }

    public boolean isCompletedOnDate(String date) {
        return completedDates.contains(date);
    }

    public int getProgress() {
        if (goalDays == 0) return 0;
        int p = (int) (((float) currentStreak / goalDays) * 100);
        return Math.min(p, 100);
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void incrementStreak() {
        this.currentStreak++;
    }

    private void decrementStreak() {
        if (this.currentStreak > 0) this.currentStreak--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getGoalDays() {
        return goalDays;
    }

    public void setGoalDays(int goalDays) {
        this.goalDays = goalDays;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}