package com.calendar.cute.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.calendar.cute.R;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.adapters.HabitAdapter;
import com.calendar.cute.dialogs.AddHabitDialog;
import com.calendar.cute.dialogs.EditHabitDialog;
import com.calendar.cute.models.Habit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HabitFragment extends Fragment {

    private RecyclerView recyclerViewHabits;
    private FloatingActionButton fabAddHabit;
    private HabitAdapter habitAdapter;
    private List<Habit> habitList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        initViews(view);
        setupRecyclerView();

        return view;
    }

    private void initViews(View view) {
        recyclerViewHabits = view.findViewById(R.id.recycler_habits);
        fabAddHabit = view.findViewById(R.id.fab_add_habit);

        habitList = new ArrayList<>();

        fabAddHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddHabitDialog();
            }
        });
    }

    private void setupRecyclerView() {
        habitAdapter = new HabitAdapter(habitList, getContext(), new HabitAdapter.OnHabitItemListener() {
            @Override
            public void onHabitCheck(Habit habit, boolean isChecked) {
                if (isChecked) {
                    habit.incrementStreak();
                }
                habitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHabitDelete(Habit habit) {
                habitList.remove(habit);
                habitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHabitEdit(Habit habit) {
                showEditHabitDialog(habit);
            }
        });

        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHabits.setAdapter(habitAdapter);

        loadSampleHabits();
    }

    private void loadSampleHabits() {
        habitList.add(new Habit("Morning Exercise", "🏃", 15, 30, "#FFB6C1"));
        habitList.add(new Habit("Read 30 Minutes", "📚", 8, 21, "#FFE4B5"));
        habitList.add(new Habit("Drink 8 Glasses Water", "💧", 22, 30, "#B0E0E6"));
        habitList.add(new Habit("Meditate", "🧘", 5, 7, "#E0BBE4"));
        habitList.add(new Habit("No Social Media", "📱", 3, 14, "#98D8C8"));

        habitAdapter.notifyDataSetChanged();
    }

    private void showAddHabitDialog() {
        AddHabitDialog dialog = new AddHabitDialog(getContext(), new AddHabitDialog.OnHabitAddedListener() {
            @Override
            public void onHabitAdded(Habit habit) {
                habitList.add(0, habit);
                habitAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void showEditHabitDialog(Habit habit) {
        EditHabitDialog dialog = new EditHabitDialog(getContext(), habit, new EditHabitDialog.OnHabitEditedListener() {
            @Override
            public void onHabitEdited(Habit editedHabit) {
                habitAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}