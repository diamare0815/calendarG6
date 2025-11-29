package com.calendar.cute.database.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.EventEntity;

import java.util.List;
@Dao
public interface EventDao {
    @Insert
    void insert(EventEntity event);

    @Update
    void update(EventEntity event);

    @Delete
    void delete(EventEntity event);

    @Query("SELECT * FROM events ORDER BY timestamp DESC")
    LiveData<List<EventEntity>> getAllEvents();

    @Query("SELECT * FROM events WHERE date = :date ORDER BY time ASC")
    LiveData<List<EventEntity>> getEventsByDate(String date);

    @Query("DELETE FROM events")
    void deleteAllEvents();
}
