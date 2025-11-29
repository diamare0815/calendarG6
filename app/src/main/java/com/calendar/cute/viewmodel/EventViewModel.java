package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.entities.EventEntity;
import com.calendar.cute.repository.*;
import java.util.List;

// ==================== EventViewModel ====================
public class EventViewModel extends AndroidViewModel {
    private EventRepository repository;
    private LiveData<List<EventEntity>> allEvents;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }

    public LiveData<List<EventEntity>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<EventEntity>> getEventsByDate(String date) {
        return repository.getEventsByDate(date);
    }

    public void insert(EventEntity event) {
        repository.insert(event);
    }

    public void update(EventEntity event) {
        repository.update(event);
    }

    public void delete(EventEntity event) {
        repository.delete(event);
    }
}