package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import androidx.annotation.NonNull;

import com.calendar.cute.models.Event;
import com.calendar.cute.R;

import java.util.Calendar;
import java.util.Locale;

public class AddEventDialog extends Dialog {

    private EditText etTitle, etTime;
    private Button btnSave, btnCancel;
    private LinearLayout colorPicker;
    private String selectedDate;
    private String selectedColor = "#FFB6C1";
    private OnEventAddedListener listener;

    private View colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow;

    public interface OnEventAddedListener {
        void onEventAdded(Event event);
    }

    public AddEventDialog(@NonNull Context context, String date, OnEventAddedListener listener) {
        super(context);
        this.selectedDate = date;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_event);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_event_title);
        etTime = findViewById(R.id.et_event_time);
        btnSave = findViewById(R.id.btn_save_event);
        btnCancel = findViewById(R.id.btn_cancel_event);

        colorPink = findViewById(R.id.color_pink);
        colorPeach = findViewById(R.id.color_peach);
        colorPurple = findViewById(R.id.color_purple);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        // Set default selection
        colorPink.setScaleX(1.2f);
        colorPink.setScaleY(1.2f);
    }

    private void setupListeners() {
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                if (!title.isEmpty() && !time.isEmpty()) {
                    Event event = new Event(title, time, selectedDate, selectedColor);
                    listener.onEventAdded(event);
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

        setupColorPickers();
    }

    private void setupColorPickers() {
        colorPink.setOnClickListener(v -> selectColor("#FFB6C1", colorPink));
        colorPeach.setOnClickListener(v -> selectColor("#FFE4B5", colorPeach));
        colorPurple.setOnClickListener(v -> selectColor("#E0BBE4", colorPurple));
        colorBlue.setOnClickListener(v -> selectColor("#B0E0E6", colorBlue));
        colorGreen.setOnClickListener(v -> selectColor("#98D8C8", colorGreen));
        colorYellow.setOnClickListener(v -> selectColor("#F0E68C", colorYellow));
    }

    private void selectColor(String color, View selectedView) {
        selectedColor = color;

        // Reset all scales
        colorPink.setScaleX(1.0f);
        colorPink.setScaleY(1.0f);
        colorPeach.setScaleX(1.0f);
        colorPeach.setScaleY(1.0f);
        colorPurple.setScaleX(1.0f);
        colorPurple.setScaleY(1.0f);
        colorBlue.setScaleX(1.0f);
        colorBlue.setScaleY(1.0f);
        colorGreen.setScaleX(1.0f);
        colorGreen.setScaleY(1.0f);
        colorYellow.setScaleX(1.0f);
        colorYellow.setScaleY(1.0f);

        // Scale selected
        selectedView.setScaleX(1.2f);
        selectedView.setScaleY(1.2f);
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int hour12 = hourOfDay % 12;
                        if (hour12 == 0) hour12 = 12;

                        String time = String.format(Locale.getDefault(), "%d:%02d %s", hour12, minuteOfHour, amPm);
                        etTime.setText(time);
                    }
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }
}
