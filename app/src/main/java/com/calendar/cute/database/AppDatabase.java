package com.calendar.cute.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.calendar.cute.database.dao.DiaryDao;
import com.calendar.cute.database.dao.EventDao;
import com.calendar.cute.database.dao.HabitDao;
import com.calendar.cute.database.dao.NoteDao;
import com.calendar.cute.database.dao.TodoDao;
import com.calendar.cute.database.entities.DiaryEntity;
import com.calendar.cute.database.entities.EventEntity;
import com.calendar.cute.database.entities.HabitEntity;
import com.calendar.cute.database.entities.NoteEntity;
import com.calendar.cute.database.entities.TodoEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        EventEntity.class,
        TodoEntity.class,
        NoteEntity.class,
        DiaryEntity.class,
        HabitEntity.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Abstract methods for DAOs
    public abstract EventDao eventDao();
    public abstract TodoDao todoDao();
    public abstract NoteDao noteDao();
    public abstract DiaryDao diaryDao();
    public abstract HabitDao habitDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;

    // ExecutorService for background operations
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Get database instance
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "cute_calendar_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}