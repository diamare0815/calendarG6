package com.calendar.cute.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
        DiaryEntity.class,
        EventEntity.class,
        HabitEntity.class,
        NoteEntity.class,
        TodoEntity.class
}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DiaryDao diaryDao();
    public abstract EventDao eventDao();
    public abstract HabitDao habitDao();
    public abstract NoteDao noteDao();
    public abstract TodoDao todoDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "cute_calendar_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                HabitDao dao = INSTANCE.habitDao();

                dao.insert(new HabitEntity(
                        "Reading book", "📚", 15, 30, 15, "#F48FB1", "2025-11-14",
                        "2025-11-14,2025-11-15,2025-11-16,2025-11-19,2025-11-20,2025-11-26,2025-11-27,2025-11-28,2025-12-01,2025-12-02,2025-12-03,2025-12-04,2025-12-05"
                ));

                dao.insert(new HabitEntity(
                        "Water 2L", "💧", 8, 60, 12, "#42A5F5", "2025-11-20",
                        "2025-11-20,2025-11-22,2025-11-24,2025-11-26,2025-11-28,2025-12-02,2025-12-04,2025-12-08"
                ));

                dao.insert(new HabitEntity(
                        "Run 30 minutes", "🏃", 4, 90, 8, "#FFB74D", "2025-12-01",
                        "2025-12-01,2025-12-05,2025-12-06,2025-12-08"
                ));
            });
        }
    };
}