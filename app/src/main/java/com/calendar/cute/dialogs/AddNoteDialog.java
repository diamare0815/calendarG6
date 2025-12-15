package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.calendar.cute.R;
import com.calendar.cute.models.Note;

public class AddNoteDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private String selectedColor = "#E6A8D7"; // Default pink
    private OnNoteAddedListener listener;

    // Color views (5 colors like in image)
    private View colorPink, colorBlue, colorGreen, colorYellow, colorPeach;

    public interface OnNoteAddedListener {
        void onNoteAdded(Note note);
    }

    public AddNoteDialog(@NonNull Context context, OnNoteAddedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_note);

        // Make dialog background transparent
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        // EditTexts
        etTitle = findViewById(R.id.et_note_title);
        etContent = findViewById(R.id.et_note_content);

        // Buttons
        btnSave = findViewById(R.id.btn_save_note);
        btnCancel = findViewById(R.id.btn_cancel_note);

        // Color views (5 colors)
        colorPink = findViewById(R.id.color_pink);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);
        colorPeach = findViewById(R.id.color_peach);

        // Set default selected color (Pink)
        selectColor("#E6A8D7", colorPink);
    }

    private void setupListeners() {
        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                // Validation
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                    etTitle.requestFocus();
                    return;
                }

                if (content.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter content", Toast.LENGTH_SHORT).show();
                    etContent.requestFocus();
                    return;
                }

                // Create note object
                Note note = new Note(title, content, selectedColor, System.currentTimeMillis());

                // Callback to listener
                if (listener != null) {
                    listener.onNoteAdded(note);
                }

                Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Setup color pickers
        setupColorPickers();
    }

    private void setupColorPickers() {
        colorPink.setOnClickListener(v -> selectColor("#E6A8D7", colorPink));
        colorBlue.setOnClickListener(v -> selectColor("#87CEEB", colorBlue));
        colorGreen.setOnClickListener(v -> selectColor("#90EE90", colorGreen));
        colorYellow.setOnClickListener(v -> selectColor("#FFE66D", colorYellow));
        colorPeach.setOnClickListener(v -> selectColor("#FFB6C1", colorPeach));
    }

    private void selectColor(String color, View selectedView) {
        selectedColor = color;

        // Reset all colors to normal size
        resetAllColorScales();

        // Scale up selected color (1.3x for more visible effect)
        selectedView.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(200)
                .start();
    }

    private void resetAllColorScales() {
        colorPink.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        colorBlue.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        colorGreen.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        colorYellow.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
        colorPeach.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
    }
}