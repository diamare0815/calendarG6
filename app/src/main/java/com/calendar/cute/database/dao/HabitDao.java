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
    @Insert
    void insert(HabitEntity habit);

    @Update
    void update(HabitEntity habit);

    @Delete
    void delete(HabitEntity habit);

    @Query("SELECT * FROM habits ORDER BY timestamp DESC")
    LiveData<List<HabitEntity>> getAllHabits();

    @Query("SELECT * FROM habits WHERE currentStreak >= goalDays")
    LiveData<List<HabitEntity>> getCompletedHabits();

    @Query("DELETE FROM habits")
    void deleteAllHabits();
}