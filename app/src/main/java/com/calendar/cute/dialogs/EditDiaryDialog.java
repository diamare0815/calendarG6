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
import androidx.annotation.NonNull;
import com.calendar.cute.R;
import com.calendar.cute.database.entities.DiaryEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDiaryDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private TextView tvDatePicker;
    private TextView moodHappy, moodSad, moodExcited, moodCalm, moodNeutral;

    private final DiaryEntity entry;
    private long selectedTimestamp;
    private String currentMood;
    private final OnDiaryEditedListener listener;

    public interface OnDiaryEditedListener {
        void onDiaryEdited(DiaryEntity entry);
    }

    public EditDiaryDialog(@NonNull Context context, DiaryEntity entry, OnDiaryEditedListener listener) {
        super(context);
        this.entry = entry;
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
        populateData();
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
    }

    private void populateData() {
        if (entry != null) {
            etTitle.setText(entry.getTitle());
            etContent.setText(entry.getContent());
            selectedTimestamp = entry.getTimestamp();
            updateDateText();

            currentMood = entry.getMood();
            if (currentMood == null) currentMood = "neutral";

            updateMoodUI(currentMood);
        }
    }

    private void setupListeners() {
        if (tvDatePicker != null) {
            tvDatePicker.setOnClickListener(v -> showDatePicker());
        }

        if (moodHappy != null) moodHappy.setOnClickListener(v -> { currentMood = "happy"; updateMoodUI("happy"); });
        if (moodSad != null) moodSad.setOnClickListener(v -> { currentMood = "sad"; updateMoodUI("sad"); });
        if (moodExcited != null) moodExcited.setOnClickListener(v -> { currentMood = "excited"; updateMoodUI("excited"); });
        if (moodCalm != null) moodCalm.setOnClickListener(v -> { currentMood = "calm"; updateMoodUI("calm"); });
        if (moodNeutral != null) moodNeutral.setOnClickListener(v -> { currentMood = "neutral"; updateMoodUI("neutral"); });

        btnSave.setOnClickListener(v -> {
            entry.setTitle(etTitle.getText().toString().trim());
            entry.setContent(etContent.getText().toString().trim());
            entry.setMood(currentMood);
            entry.setTimestamp(selectedTimestamp);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            entry.setDate(sdf.format(new Date(selectedTimestamp)));

            listener.onDiaryEdited(entry);
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void updateMoodUI(String mood) {
        resetScale(moodHappy);
        resetScale(moodSad);
        resetScale(moodExcited);
        resetScale(moodCalm);
        resetScale(moodNeutral);

        TextView targetView = null;
        if (mood != null) {
            switch (mood.toLowerCase()) {
                case "happy": targetView = moodHappy; break;
                case "sad": targetView = moodSad; break;
                case "excited": targetView = moodExcited; break;
                case "calm": targetView = moodCalm; break;
                case "neutral": targetView = moodNeutral; break;
            }
        }

        if (targetView != null) {
            targetView.setScaleX(1.2f);
            targetView.setScaleY(1.2f);
        }
    }

    private void resetScale(View v) {
        if (v != null) {
            v.setScaleX(1.0f);
            v.setScaleY(1.0f);
        }
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