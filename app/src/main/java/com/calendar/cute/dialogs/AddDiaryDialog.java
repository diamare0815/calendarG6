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
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.calendar.cute.models.DiaryEntry;
import com.calendar.cute.R;

public class AddDiaryDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private TextView moodHappy, moodSad, moodExcited, moodCalm, moodNeutral;
    private String selectedMood = "neutral";
    private OnDiaryAddedListener listener;

    public interface OnDiaryAddedListener {
        void onDiaryAdded(DiaryEntry entry);
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

        initViews();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_diary_title);
        etContent = findViewById(R.id.et_diary_content);
        btnSave = findViewById(R.id.btn_save_diary);
        btnCancel = findViewById(R.id.btn_cancel_diary);

        moodHappy = findViewById(R.id.mood_happy);
        moodSad = findViewById(R.id.mood_sad);
        moodExcited = findViewById(R.id.mood_excited);
        moodCalm = findViewById(R.id.mood_calm);
        moodNeutral = findViewById(R.id.mood_neutral);

        moodNeutral.setScaleX(1.2f);
        moodNeutral.setScaleY(1.2f);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault());
                    String date = sdf.format(new java.util.Date());

                    DiaryEntry entry = new DiaryEntry(title, content, selectedMood, date, System.currentTimeMillis());
                    listener.onDiaryAdded(entry);
                    dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setupMoodSelectors();
    }

    private void setupMoodSelectors() {
        moodHappy.setOnClickListener(v -> selectMood("happy", moodHappy));
        moodSad.setOnClickListener(v -> selectMood("sad", moodSad));
        moodExcited.setOnClickListener(v -> selectMood("excited", moodExcited));
        moodCalm.setOnClickListener(v -> selectMood("calm", moodCalm));
        moodNeutral.setOnClickListener(v -> selectMood("neutral", moodNeutral));
    }

    private void selectMood(String mood, TextView selectedView) {
        selectedMood = mood;

        moodHappy.setScaleX(1.0f);
        moodHappy.setScaleY(1.0f);
        moodSad.setScaleX(1.0f);
        moodSad.setScaleY(1.0f);
        moodExcited.setScaleX(1.0f);
        moodExcited.setScaleY(1.0f);
        moodCalm.setScaleX(1.0f);
        moodCalm.setScaleY(1.0f);
        moodNeutral.setScaleX(1.0f);
        moodNeutral.setScaleY(1.0f);

        selectedView.setScaleX(1.2f);
        selectedView.setScaleY(1.2f);
    }
}