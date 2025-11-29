package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.entities.NoteEntity;
import com.calendar.cute.repository.*;
import java.util.List;

class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return repository.searchNotes(query);
    }

    public void insert(NoteEntity note) {
        repository.insert(note);
    }

    public void update(NoteEntity note) {
        repository.update(note);
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }
}