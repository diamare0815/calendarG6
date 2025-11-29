package com.calendar.cute.database.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.DiaryEntity;

import java.util.List;
@Dao
public interface DiaryDao {
    @Insert
    void insert(DiaryEntity diary);

    @Update
    void update(DiaryEntity diary);

    @Delete
    void delete(DiaryEntity diary);

    @Query("SELECT * FROM diary ORDER BY timestamp DESC")
    LiveData<List<DiaryEntity>> getAllDiaries();

    @Query("SELECT * FROM diary WHERE mood = :mood ORDER BY timestamp DESC")
    LiveData<List<DiaryEntity>> getDiariesByMood(String mood);

    @Query("SELECT * FROM diary WHERE date = :date")
    LiveData<DiaryEntity> getDiaryByDate(String date);

    @Query("DELETE FROM diary")
    void deleteAllDiaries();
}

