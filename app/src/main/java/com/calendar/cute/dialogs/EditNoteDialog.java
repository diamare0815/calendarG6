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
import com.calendar.cute.R;

import com.calendar.cute.models.Note;

public class EditNoteDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private Note note;
    private OnNoteEditedListener listener;

    public interface OnNoteEditedListener {
        void onNoteEdited(Note note);
    }

    public EditNoteDialog(@NonNull Context context, Note note, OnNoteEditedListener listener) {
        super(context);
        this.note = note;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_note);

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_note_title);
        etContent = findViewById(R.id.et_note_content);
        btnSave = findViewById(R.id.btn_save_note);
        btnCancel = findViewById(R.id.btn_cancel_note);
    }

    private void populateData() {
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(etTitle.getText().toString().trim());
                note.setContent(etContent.getText().toString().trim());
                note.setTimestamp(System.currentTimeMillis());

                listener.onNoteEdited(note);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}
