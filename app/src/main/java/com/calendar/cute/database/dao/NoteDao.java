package com.calendar.cute.database.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.NoteEntity;

import java.util.List;
@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :searchQuery OR content LIKE :searchQuery ORDER BY timestamp DESC")
    LiveData<List<NoteEntity>> searchNotes(String searchQuery);

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}