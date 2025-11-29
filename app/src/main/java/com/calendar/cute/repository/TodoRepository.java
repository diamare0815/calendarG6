package com.calendar.cute.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.calendar.cute.database.*;
import com.calendar.cute.database.dao.TodoDao;
import com.calendar.cute.database.entities.TodoEntity;

import java.util.List;
public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<TodoEntity>> allTodos;

    public TodoRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        todoDao = database.todoDao();
        allTodos = todoDao.getAllTodos();
    }

    public LiveData<List<TodoEntity>> getAllTodos() {
        return allTodos;
    }

    public LiveData<List<TodoEntity>> getPendingTodos() {
        return todoDao.getPendingTodos();
    }

    public LiveData<List<TodoEntity>> getCompletedTodos() {
        return todoDao.getCompletedTodos();
    }

    public LiveData<List<TodoEntity>> getImportantTodos() {
        return todoDao.getImportantTodos();
    }

    public void insert(TodoEntity todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.insert(todo);
        });
    }

    public void update(TodoEntity todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.update(todo);
        });
    }

    public void delete(TodoEntity todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.delete(todo);
        });
    }
}