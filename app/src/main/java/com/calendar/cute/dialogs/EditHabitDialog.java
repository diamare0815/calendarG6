package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.calendar.cute.R;
import com.calendar.cute.models.Habit;

public class EditHabitDialog extends Dialog {

    private EditText etName, etGoal;
    private Button btnSave, btnCancel;
    private final Habit habit;
    private final OnHabitEditedListener listener;

    public interface OnHabitEditedListener {
        void onHabitEdited(Habit habit);
    }

    public EditHabitDialog(@NonNull Context context, Habit habit, OnHabitEditedListener listener) {
        super(context);
        this.habit = habit;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_habit);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            int dialogWidth = (int) (screenWidth * 0.90);
            window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_habit_name);
        etGoal = findViewById(R.id.et_habit_goal);
        btnSave = findViewById(R.id.btn_save_habit);
        btnCancel = findViewById(R.id.btn_cancel_habit);
    }

    private void populateData() {
        if (habit != null) {
            etName.setText(habit.getName());
            etGoal.setText(String.valueOf(habit.getGoalDays()));
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String goalStr = etGoal.getText().toString().trim();

            if (!name.isEmpty() && !goalStr.isEmpty()) {
                habit.setName(name);
                habit.setGoalDays(Integer.parseInt(goalStr));
                listener.onHabitEdited(habit);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}