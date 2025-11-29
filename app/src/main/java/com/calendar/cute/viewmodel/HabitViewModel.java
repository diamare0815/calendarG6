package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.entities.HabitEntity;
import com.calendar.cute.repository.*;
import java.util.List;
class HabitViewModel extends AndroidViewModel {
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

    public LiveData<List<HabitEntity>> getCompletedHabits() {
        return repository.getCompletedHabits();
    }

    public void insert(HabitEntity habit) {
        repository.insert(habit);
    }

    public void update(HabitEntity habit) {
        repository.update(habit);
    }

    public void delete(HabitEntity habit) {
        repository.delete(habit);
    }
}
