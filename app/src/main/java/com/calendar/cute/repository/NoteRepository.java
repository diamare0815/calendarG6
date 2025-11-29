package com.calendar.cute.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.dao.NoteDao;
import com.calendar.cute.database.entities.NoteEntity;

import java.util.List;
public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return noteDao.searchNotes("%" + query + "%");
    }

    public void insert(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.insert(note);
        });
    }

    public void update(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.update(note);
        });
    }

    public void delete(NoteEntity note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.delete(note);
        });
    }
}