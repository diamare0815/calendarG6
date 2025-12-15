package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import com.calendar.cute.R;
import com.calendar.cute.models.TodoItem;

public class EditTodoDialog extends Dialog {

    private EditText etTitle;
    private Spinner spinnerCategory;
    private CheckBox cbImportant;
    private Button btnSave, btnCancel;
    private TodoItem todoItem;
    private OnTodoEditedListener listener;

    public interface OnTodoEditedListener {
        void onTodoEdited(TodoItem item);
    }

    public EditTodoDialog(@NonNull Context context, TodoItem item, OnTodoEditedListener listener) {
        super(context);
        this.todoItem = item;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_todo);

        // Fix kích thước dialog
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);

            window.setLayout(
                    (int)(getContext().getResources().getDisplayMetrics().widthPixels * 0.98),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_todo_title);
        spinnerCategory = findViewById(R.id.spinner_category);
        cbImportant = findViewById(R.id.cb_important);
        btnSave = findViewById(R.id.btn_save_todo);
        btnCancel = findViewById(R.id.btn_cancel_todo);

        String[] categories = {"Personal", "Work", "Health", "Shopping", "Study", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);
    }

    private void populateData() {
        etTitle.setText(todoItem.getTitle());
        cbImportant.setChecked(todoItem.isImportant());

        String[] categories = {"Personal", "Work", "Health", "Shopping", "Study", "Other"};
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(todoItem.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                if (!title.isEmpty()) {
                    todoItem.setTitle(title);
                    todoItem.setCategory(spinnerCategory.getSelectedItem().toString());
                    todoItem.setImportant(cbImportant.isChecked());

                    listener.onTodoEdited(todoItem);
                    dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}