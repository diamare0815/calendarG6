package com.calendar.cute.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.calendar.cute.R;
import com.calendar.cute.models.Habit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddHabitDialog extends Dialog {

    private EditText etName, etGoal;
    private Button btnSave, btnCancel;
    private TextView tvStartDate;

    private TextView iconRun, iconBook, iconWater, iconMeditation, iconPhone;
    private TextView iconSleep, iconFood, iconMuscle, iconHeart, iconStar;
    private View colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow;

    private String selectedIcon = "🏃";
    private String selectedColor = "#FFB6C1";
    private final Calendar selectedDate = Calendar.getInstance();

    private final OnHabitAddedListener listener;

    public interface OnHabitAddedListener {
        void onHabitAdded(Habit habit);
    }

    public AddHabitDialog(@NonNull Context context, OnHabitAddedListener listener) {
        super(context);
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
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_habit_name);
        etGoal = findViewById(R.id.et_habit_goal);
        tvStartDate = findViewById(R.id.tv_start_date);
        btnSave = findViewById(R.id.btn_save_habit);
        btnCancel = findViewById(R.id.btn_cancel_habit);

        iconRun = findViewById(R.id.icon_run);
        iconBook = findViewById(R.id.icon_book);
        iconWater = findViewById(R.id.icon_water);
        iconMeditation = findViewById(R.id.icon_meditation);
        iconPhone = findViewById(R.id.icon_phone);
        iconSleep = findViewById(R.id.icon_sleep);
        iconFood = findViewById(R.id.icon_food);
        iconMuscle = findViewById(R.id.icon_muscle);
        iconHeart = findViewById(R.id.icon_heart);
        iconStar = findViewById(R.id.icon_star);

        colorPink = findViewById(R.id.color_pink);
        colorPeach = findViewById(R.id.color_peach);
        colorPurple = findViewById(R.id.color_purple);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        iconRun.setScaleX(1.2f);
        iconRun.setScaleY(1.2f);
        colorPink.setScaleX(1.2f);
        colorPink.setScaleY(1.2f);

        updateDateDisplay();
    }

    private void setupListeners() {
        tvStartDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String goalStr = etGoal.getText().toString().trim();
            String startDate = tvStartDate.getText().toString();

            if (!name.isEmpty() && !goalStr.isEmpty()) {
                int goal = Integer.parseInt(goalStr);
                Habit habit = new Habit(name, selectedIcon, 0, goal, selectedColor, startDate);
                listener.onHabitAdded(habit);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill name and goal", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        setupIconSelectors();
        setupColorPickers();
    }

    private void setupIconSelectors() {
        iconRun.setOnClickListener(v -> selectIcon("🏃", iconRun));
        iconBook.setOnClickListener(v -> selectIcon("📚", iconBook));
        iconWater.setOnClickListener(v -> selectIcon("💧", iconWater));
        iconMeditation.setOnClickListener(v -> selectIcon("🧘", iconMeditation));
        iconPhone.setOnClickListener(v -> selectIcon("📱", iconPhone));
        iconSleep.setOnClickListener(v -> selectIcon("😴", iconSleep));
        iconFood.setOnClickListener(v -> selectIcon("🥗", iconFood));
        iconMuscle.setOnClickListener(v -> selectIcon("💪", iconMuscle));
        iconHeart.setOnClickListener(v -> selectIcon("❤️", iconHeart));
        iconStar.setOnClickListener(v -> selectIcon("⭐", iconStar));
    }

    private void setupColorPickers() {
        colorPink.setOnClickListener(v -> selectColor("#FFB6C1", colorPink));
        colorPeach.setOnClickListener(v -> selectColor("#FFE4B5", colorPeach));
        colorPurple.setOnClickListener(v -> selectColor("#E0BBE4", colorPurple));
        colorBlue.setOnClickListener(v -> selectColor("#B0E0E6", colorBlue));
        colorGreen.setOnClickListener(v -> selectColor("#98D8C8", colorGreen));
        colorYellow.setOnClickListener(v -> selectColor("#F0E68C", colorYellow));
    }

    private void selectIcon(String icon, TextView selectedView) {
        selectedIcon = icon;

        iconRun.setScaleX(1.0f); iconRun.setScaleY(1.0f);
        iconBook.setScaleX(1.0f); iconBook.setScaleY(1.0f);
        iconWater.setScaleX(1.0f); iconWater.setScaleY(1.0f);
        iconMeditation.setScaleX(1.0f); iconMeditation.setScaleY(1.0f);
        iconPhone.setScaleX(1.0f); iconPhone.setScaleY(1.0f);
        iconSleep.setScaleX(1.0f); iconSleep.setScaleY(1.0f);
        iconFood.setScaleX(1.0f); iconFood.setScaleY(1.0f);
        iconMuscle.setScaleX(1.0f); iconMuscle.setScaleY(1.0f);
        iconHeart.setScaleX(1.0f); iconHeart.setScaleY(1.0f);
        iconStar.setScaleX(1.0f); iconStar.setScaleY(1.0f);

        selectedView.setScaleX(1.2f);
        selectedView.setScaleY(1.2f);
    }

    private void selectColor(String color, View selectedView) {
        selectedColor = color;

        colorPink.setScaleX(1.0f); colorPink.setScaleY(1.0f);
        colorPeach.setScaleX(1.0f); colorPeach.setScaleY(1.0f);
        colorPurple.setScaleX(1.0f); colorPurple.setScaleY(1.0f);
        colorBlue.setScaleX(1.0f); colorBlue.setScaleY(1.0f);
        colorGreen.setScaleX(1.0f); colorGreen.setScaleY(1.0f);
        colorYellow.setScaleX(1.0f); colorYellow.setScaleY(1.0f);

        selectedView.setScaleX(1.2f);
        selectedView.setScaleY(1.2f);
    }

    private void showDatePicker() {
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateDisplay();
        },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tvStartDate.setText(sdf.format(selectedDate.getTime()));
    }
}