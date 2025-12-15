package com.calendar.cute.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.HabitEntity;

import java.util.List;

@Dao
public interface HabitDao {

    @Query("SELECT * FROM habits")
    LiveData<List<HabitEntity>> getAllHabits();

    @Insert
    long insert(HabitEntity habit);

    @Update
    void update(HabitEntity habit);

    @Delete
    void delete(HabitEntity habit);

    @Query("UPDATE habits SET completedDates = :dates WHERE id = :habitId")
    void updateCompletedDates(int habitId, String dates);
}