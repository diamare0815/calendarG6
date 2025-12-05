package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calendar.cute.R;
import com.calendar.cute.models.Event;

import java.util.Calendar;
import java.util.Locale;
import android.graphics.drawable.ColorDrawable;

public class AddEventDialog extends Dialog {

    private EditText etTitle, etTime;
    private Button btnSave, btnCancel;
    private View colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow;

    private String selectedDate;
    private String selectedColor = "#FFB6C1";
    private Event editEvent = null;

    private OnEventAddedListener listener;

    public interface OnEventAddedListener {
        void onEventAdded(Event event);
    }

    // Constructor cho thêm mới
    public AddEventDialog(@NonNull Context context, @Nullable String date, @Nullable OnEventAddedListener listener) {
        super(context);
        this.selectedDate = date;
        this.listener = listener;
    }

    // Constructor cho sửa
    public AddEventDialog(@NonNull Context context, @Nullable Event event, @Nullable OnEventAddedListener listener) {
        super(context);
        this.editEvent = event;
        this.selectedDate = event.getDate();
        this.selectedColor = event.getColor();
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_event);

        Window window = getWindow();
        if(window!=null){
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(android.view.Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
        }

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

        if(editEvent != null){
            etTitle.setText(editEvent.getTitle());
            etTime.setText(editEvent.getTime());
            highlightSelectedColorViewByColor(editEvent.getColor());
        } else {
            highlightSelectedColorViewByColor(selectedColor);
        }

        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> handleSave());
        btnCancel.setOnClickListener(v -> dismiss());

        colorPink.setOnClickListener(v -> selectColor("#FFB6C1", colorPink));
        colorPeach.setOnClickListener(v -> selectColor("#FFE4B5", colorPeach));
        colorPurple.setOnClickListener(v -> selectColor("#E0BBE4", colorPurple));
        colorBlue.setOnClickListener(v -> selectColor("#B0E0E6", colorBlue));
        colorGreen.setOnClickListener(v -> selectColor("#98D8C8", colorGreen));
        colorYellow.setOnClickListener(v -> selectColor("#F0E68C", colorYellow));
    }

    private void selectColor(String color, View selectedView){
        selectedColor = color;
        resetColorHighlights();
        highlightSelectedColorView(selectedView);
    }

    private void resetColorHighlights(){
        colorPink.setScaleX(1f); colorPink.setScaleY(1f);
        colorPeach.setScaleX(1f); colorPeach.setScaleY(1f);
        colorPurple.setScaleX(1f); colorPurple.setScaleY(1f);
        colorBlue.setScaleX(1f); colorBlue.setScaleY(1f);
        colorGreen.setScaleX(1f); colorGreen.setScaleY(1f);
        colorYellow.setScaleX(1f); colorYellow.setScaleY(1f);
    }

    private void highlightSelectedColorView(View v){
        if(v!=null){ v.setScaleX(1.2f); v.setScaleY(1.2f);}
    }

    private void highlightSelectedColorViewByColor(String color){
        if(color.equals("#FFB6C1")) highlightSelectedColorView(colorPink);
        else if(color.equals("#FFE4B5")) highlightSelectedColorView(colorPeach);
        else if(color.equals("#E0BBE4")) highlightSelectedColorView(colorPurple);
        else if(color.equals("#B0E0E6")) highlightSelectedColorView(colorBlue);
        else if(color.equals("#98D8C8")) highlightSelectedColorView(colorGreen);
        else if(color.equals("#F0E68C")) highlightSelectedColorView(colorYellow);
    }

    private void handleSave(){
        String title = etTitle.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(getContext(), "Nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(time)){
            Toast.makeText(getContext(), "Chọn thời gian", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, time, selectedDate, selectedColor);
        if(listener!=null) listener.onEventAdded(event);
        dismiss();
    }

    private void showTimePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (TimePicker view, int hourOfDay, int minuteOfHour) -> {
                    String amPm = hourOfDay >=12 ? "PM":"AM";
                    int hour12 = hourOfDay%12; if(hour12==0) hour12=12;
                    String timeStr = String.format(Locale.getDefault(), "%d:%02d %s", hour12, minuteOfHour, amPm);
                    etTime.setText(timeStr);
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public void setOnEventAddedListener(OnEventAddedListener listener){
        this.listener = listener;
    }
}
