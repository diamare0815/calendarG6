package com.calendar.cute.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.calendar.cute.R;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.calendar.cute.models.Habit;

public class EditHabitDialog extends Dialog {

    private EditText etName, etGoal;
    private Button btnSave, btnCancel;
    private Habit habit;
    private OnHabitEditedListener listener;

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
        etName.setText(habit.getName());
        etGoal.setText(String.valueOf(habit.getGoalDays()));
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit.setName(etName.getText().toString().trim());
                habit.setGoalDays(Integer.parseInt(etGoal.getText().toString().trim()));

                listener.onHabitEdited(habit);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}