package com.calendar.cute;
import com.calendar.cute.R;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.calendar.cute.fragments.CalendarFragment;
import com.calendar.cute.fragments.HabitFragment;
import com.calendar.cute.fragments.NotesFragment;
import com.calendar.cute.fragments.TodoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new CalendarFragment());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_calendar) {
                    fragment = new CalendarFragment();
                } else if (itemId == R.id.nav_todo) {
                    fragment = new TodoFragment();
                } else if (itemId == R.id.nav_notes) {
                    fragment = new NotesFragment();
                } else if (itemId == R.id.nav_diary) {
                    fragment = new com.calendar.cute.fragments.DiaryFragment();
                } else if (itemId == R.id.nav_habit) {
                    fragment = new HabitFragment();
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}