package com.calendar.cute.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.dao.EventDao;
import com.calendar.cute.database.entities.EventEntity;

import java.util.List;
public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<EventEntity>> allEvents;

    public EventRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        eventDao = database.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public LiveData<List<EventEntity>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<EventEntity>> getEventsByDate(String date) {
        return eventDao.getEventsByDate(date);
    }

    public void insert(EventEntity event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }

    public void update(EventEntity event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(event);
        });
    }

    public void delete(EventEntity event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(event);
        });
    }
}