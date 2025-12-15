package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.calendar.cute.database.entities.HabitEntity;
import com.calendar.cute.models.Habit;
import com.calendar.cute.repository.HabitRepository;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private HabitRepository repository;
    private LiveData<List<HabitEntity>> allHabits;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitRepository(application);
        allHabits = repository.getAllHabits();
    }

    public LiveData<List<HabitEntity>> getAllHabits() {
        return allHabits;
    }

    public void insert(Habit habit) {
        repository.insert(habit.toEntity());
    }

    public void delete(Habit habit) {
        repository.delete(habit.toEntity());
    }

    public void update(Habit habit) {
        repository.update(habit.toEntity());
    }
}