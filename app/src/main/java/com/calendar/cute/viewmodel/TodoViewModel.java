package com.calendar.cute.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.entities.TodoEntity;
import com.calendar.cute.repository.*;
import java.util.List;

// ==================== EventViewModel ====================
class TodoViewModel extends AndroidViewModel {
    private TodoRepository repository;
    private LiveData<List<TodoEntity>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodos();
    }

    public LiveData<List<TodoEntity>> getAllTodos() {
        return allTodos;
    }

    public LiveData<List<TodoEntity>> getPendingTodos() {
        return repository.getPendingTodos();
    }

    public LiveData<List<TodoEntity>> getCompletedTodos() {
        return repository.getCompletedTodos();
    }

    public LiveData<List<TodoEntity>> getImportantTodos() {
        return repository.getImportantTodos();
    }

    public void insert(TodoEntity todo) {
        repository.insert(todo);
    }

    public void update(TodoEntity todo) {
        repository.update(todo);
    }

    public void delete(TodoEntity todo) {
        repository.delete(todo);
    }
}