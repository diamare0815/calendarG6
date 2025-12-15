package com.calendar.cute.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.calendar.cute.R;
import com.calendar.cute.database.entities.DiaryEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDiaryDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private TextView tvDatePicker;
    private TextView moodHappy, moodSad, moodExcited, moodCalm, moodNeutral;

    private String selectedMood = "neutral";
    private long selectedTimestamp;
    private final OnDiaryAddedListener listener;

    public interface OnDiaryAddedListener {
        void onDiaryAdded(DiaryEntity entry);
    }

    public AddDiaryDialog(@NonNull Context context, OnDiaryAddedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_diary);

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
        etTitle = findViewById(R.id.et_diary_title);
        etContent = findViewById(R.id.et_diary_content);
        btnSave = findViewById(R.id.btn_save_diary);
        btnCancel = findViewById(R.id.btn_cancel_diary);
        tvDatePicker = findViewById(R.id.tv_date_picker);

        moodHappy = findViewById(R.id.mood_happy);
        moodSad = findViewById(R.id.mood_sad);
        moodExcited = findViewById(R.id.mood_excited);
        moodCalm = findViewById(R.id.mood_calm);
        moodNeutral = findViewById(R.id.mood_neutral);

        selectedTimestamp = System.currentTimeMillis();
        updateDateText();

        if (moodNeutral != null) {
            moodNeutral.setScaleX(1.2f);
            moodNeutral.setScaleY(1.2f);
        }
    }

    private void setupListeners() {
        if (tvDatePicker != null) {
            tvDatePicker.setOnClickListener(v -> showDatePicker());
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                String dateString = sdf.format(new Date(selectedTimestamp));

                DiaryEntity entry = new DiaryEntity(title, content, selectedMood, dateString, selectedTimestamp);
                listener.onDiaryAdded(entry);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
        setupMoodSelectors();
    }

    private void setupMoodSelectors() {
        if (moodHappy == null) return;

        moodHappy.setOnClickListener(v -> selectMood("happy", moodHappy));
        moodSad.setOnClickListener(v -> selectMood("sad", moodSad));
        moodExcited.setOnClickListener(v -> selectMood("excited", moodExcited));
        moodCalm.setOnClickListener(v -> selectMood("calm", moodCalm));
        moodNeutral.setOnClickListener(v -> selectMood("neutral", moodNeutral));
    }

    private void selectMood(String mood, TextView selectedView) {
        selectedMood = mood;

        moodHappy.setScaleX(1.0f); moodHappy.setScaleY(1.0f);
        moodSad.setScaleX(1.0f); moodSad.setScaleY(1.0f);
        moodExcited.setScaleX(1.0f); moodExcited.setScaleY(1.0f);
        moodCalm.setScaleX(1.0f); moodCalm.setScaleY(1.0f);
        moodNeutral.setScaleX(1.0f); moodNeutral.setScaleY(1.0f);

        selectedView.setScaleX(1.2f);
        selectedView.setScaleY(1.2f);
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        if (tvDatePicker != null) {
            tvDatePicker.setText(sdf.format(new Date(selectedTimestamp)));
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedTimestamp);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, month, dayOfMonth);
                    selectedTimestamp = newDate.getTimeInMillis();
                    updateDateText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}