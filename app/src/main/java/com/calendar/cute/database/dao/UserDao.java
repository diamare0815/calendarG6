package com.calendar.cute.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calendar.cute.database.entities.UserEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("SELECT * FROM user ORDER BY id ASC")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    LiveData<UserEntity> getUserById(int userId);

    @Query("DELETE FROM user")
    void deleteAllUsers();
}
