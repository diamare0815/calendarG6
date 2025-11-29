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
import androidx.annotation.NonNull;
import com.calendar.cute.R;

import com.calendar.cute.models.TodoItem;

public class AddTodoDialog extends Dialog {

    private EditText etTitle;
    private Spinner spinnerCategory;
    private CheckBox cbImportant;
    private Button btnSave, btnCancel;
    private String selectedColor = "#FFB6C1";
    private OnTodoAddedListener listener;

    private View colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow;

    public interface OnTodoAddedListener {
        void onTodoAdded(TodoItem item);
    }

    public AddTodoDialog(@NonNull Context context, OnTodoAddedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_todo);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_todo_title);
        spinnerCategory = findViewById(R.id.spinner_category);
        cbImportant = findViewById(R.id.cb_important);
        btnSave = findViewById(R.id.btn_save_todo);
        btnCancel = findViewById(R.id.btn_cancel_todo);

        colorPink = findViewById(R.id.color_pink);
        colorPeach = findViewById(R.id.color_peach);
        colorPurple = findViewById(R.id.color_purple);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        // Setup spinner
        String[] categories = {"Personal", "Work", "Health", "Shopping", "Study", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // Set default selection
        colorPink.setScaleX(1.2f);
        colorPink.setScaleY(1.2f);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                boolean important = cbImportant.isChecked();

                if (!title.isEmpty()) {
                    TodoItem item = new TodoItem(title, category, false, important, selectedColor);
                    listener.onTodoAdded(item);
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
}