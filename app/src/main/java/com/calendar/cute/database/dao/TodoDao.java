package com.calendar.cute.database.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.TodoEntity;

import java.util.List;
@Dao
public interface TodoDao {
    @Insert
    void insert(TodoEntity todo);

    @Update
    void update(TodoEntity todo);

    @Delete
    void delete(TodoEntity todo);

    @Query("SELECT * FROM todos ORDER BY important DESC, timestamp DESC")
    LiveData<List<TodoEntity>> getAllTodos();

    @Query("SELECT * FROM todos WHERE completed = 0 ORDER BY important DESC, timestamp DESC")
    LiveData<List<TodoEntity>> getPendingTodos();

    @Query("SELECT * FROM todos WHERE completed = 1 ORDER BY timestamp DESC")
    LiveData<List<TodoEntity>> getCompletedTodos();

    @Query("SELECT * FROM todos WHERE important = 1 ORDER BY timestamp DESC")
    LiveData<List<TodoEntity>> getImportantTodos();

    @Query("DELETE FROM todos")
    void deleteAllTodos();
}