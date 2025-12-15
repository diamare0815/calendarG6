package com.calendar.cute.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.calendar.cute.database.AppDatabase;
import com.calendar.cute.database.dao.HabitDao;
import com.calendar.cute.database.entities.HabitEntity;

import java.util.List;

public class HabitRepository {
    private HabitDao habitDao;
    private LiveData<List<HabitEntity>> allHabits;

    public HabitRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        habitDao = database.habitDao();
        allHabits = habitDao.getAllHabits();
    }

    public LiveData<List<HabitEntity>> getAllHabits() {
        return allHabits;
    }

    public void insert(HabitEntity habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.insert(habit));
    }

    public void delete(HabitEntity habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.delete(habit));
    }

    public void update(HabitEntity habit) {
        AppDatabase.databaseWriteExecutor.execute(() -> habitDao.update(habit));
    }
}