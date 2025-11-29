package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.entities.DiaryEntity;
import com.calendar.cute.repository.*;
import java.util.List;
class DiaryViewModel extends AndroidViewModel {
    private DiaryRepository repository;
    private LiveData<List<DiaryEntity>> allDiaries;

    public DiaryViewModel(@NonNull Application application) {
        super(application);
        repository = new DiaryRepository(application);
        allDiaries = repository.getAllDiaries();
    }

    public LiveData<List<DiaryEntity>> getAllDiaries() {
        return allDiaries;
    }

    public LiveData<List<DiaryEntity>> getDiariesByMood(String mood) {
        return repository.getDiariesByMood(mood);
    }

    public LiveData<DiaryEntity> getDiaryByDate(String date) {
        return repository.getDiaryByDate(date);
    }

    public void insert(DiaryEntity diary) {
        repository.insert(diary);
    }

    public void update(DiaryEntity diary) {
        repository.update(diary);
    }

    public void delete(DiaryEntity diary) {
        repository.delete(diary);
    }
}
