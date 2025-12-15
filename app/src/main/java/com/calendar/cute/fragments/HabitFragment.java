package com.calendar.cute.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.adapters.HabitAdapter;
import com.calendar.cute.database.entities.HabitEntity;
import com.calendar.cute.dialogs.AddHabitDialog;
import com.calendar.cute.dialogs.EditHabitDialog;
import com.calendar.cute.dialogs.StatsDialog;
import com.calendar.cute.models.Habit;
import com.calendar.cute.viewmodel.HabitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {

    private RecyclerView recyclerViewHabits;
    private FloatingActionButton fabAddHabit;
    private TextView tvTotalHabits, tvDoneToday, tvBestStreak;

    private HabitAdapter habitAdapter;
    private final List<Habit> habitList = new ArrayList<>();
    private HabitViewModel habitViewModel;

    private boolean hasShownStartDialog = false;
    private boolean hasShownCongratsDialog = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        initViews(view);
        setupRecyclerView();
        observeData();

        return view;
    }

    private void initViews(View view) {
        recyclerViewHabits = view.findViewById(R.id.recycler_habits);
        fabAddHabit = view.findViewById(R.id.fab_add_habit);
        tvTotalHabits = view.findViewById(R.id.tv_total_habits);
        tvDoneToday = view.findViewById(R.id.tv_completed_today);
        tvBestStreak = view.findViewById(R.id.tv_best_streak);

        fabAddHabit.setOnClickListener(v -> showAddHabitDialog());

        View btnStats = view.findViewById(R.id.btn_view_stats);
        if (btnStats != null) {
            btnStats.setOnClickListener(v -> {
                StatsDialog dialog = new StatsDialog(getContext(), habitList);
                dialog.show();
            });
        }
    }

    private void setupRecyclerView() {
        habitAdapter = new HabitAdapter(habitList, getContext(), new HabitAdapter.OnHabitItemListener() {
            @Override
            public void onHabitClick(Habit habit) {
                habit.toggleToday();
                habitViewModel.update(habit);
            }

            @Override
            public void onHabitDelete(Habit habit) {
                showDeleteConfirm(habit);
            }

            @Override
            public void onHabitEdit(Habit habit) {
                showEditHabitDialog(habit);
            }
        });

        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHabits.setAdapter(habitAdapter);
    }

    private void observeData() {
        habitViewModel.getAllHabits().observe(getViewLifecycleOwner(), entities -> {
            habitList.clear();

            for (HabitEntity entity : entities) {
                habitList.add(new Habit(entity));
            }
            habitAdapter.notifyDataSetChanged();
            updateNumbers();
        });
    }

    private void updateNumbers() {
        if (habitList == null) return;
        if (tvTotalHabits != null) tvTotalHabits.setText(String.valueOf(habitList.size()));

        int doneCount = 0;
        int maxStreak = 0;

        for (Habit h : habitList) {
            if (h.isCompletedToday()) doneCount++;
            if (h.getCurrentStreak() > maxStreak) maxStreak = h.getCurrentStreak();
        }

        if (tvDoneToday != null) tvDoneToday.setText(String.valueOf(doneCount));
        if (tvBestStreak != null) tvBestStreak.setText(String.valueOf(maxStreak));

        checkDailyStatus();
    }

    private void checkDailyStatus() {
        if (habitList == null || habitList.isEmpty()) {
            return;
        }
        int total = habitList.size();
        int doneCount = 0;
        for (Habit h : habitList) {
            if (h.isCompletedToday()) {
                doneCount++;
            }
        }

        if (doneCount == 0 && !hasShownStartDialog) {
            showMotivationDialog(
                    "Daily check-in!",
                    "Hi there! We noticed you haven't tracked any habits today yet.\n\n Would you like to start now?"
            );
            hasShownStartDialog = true;
        } else if (doneCount == total && !hasShownCongratsDialog && total > 0) {
            showMotivationDialog(
                    "Wonderful work! ",
                    "Congratulations! You have completed all your habits for today."
            );
            hasShownCongratsDialog = true;
        }
    }

    private void showDeleteConfirm(Habit habit) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete habit")
                .setMessage("Are you sure you want to delete '" + habit.getName() + "'?")
                .setPositiveButton("Yes", (dialog, which) -> habitViewModel.delete(habit))
                .setNegativeButton("No", null)
                .show();
    }

    private void showAddHabitDialog() {
        AddHabitDialog dialog = new AddHabitDialog(getContext(), habit -> habitViewModel.insert(habit));
        dialog.show();
    }

    private void showEditHabitDialog(Habit habit) {
        EditHabitDialog dialog = new EditHabitDialog(getContext(), habit, editedHabit -> habitViewModel.update(editedHabit));
        dialog.show();
    }

    private void showMotivationDialog(String title, String message) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}