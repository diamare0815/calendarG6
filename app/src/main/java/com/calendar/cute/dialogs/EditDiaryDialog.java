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
import com.calendar.cute.models.DiaryEntry;

public class EditDiaryDialog extends Dialog {

    private EditText etTitle, etContent;
    private Button btnSave, btnCancel;
    private DiaryEntry entry;
    private OnDiaryEditedListener listener;

    public interface OnDiaryEditedListener {
        void onDiaryEdited(DiaryEntry entry);
    }

    public EditDiaryDialog(@NonNull Context context, DiaryEntry entry, OnDiaryEditedListener listener) {
        super(context);
        this.entry = entry;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_diary);

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_diary_title);
        etContent = findViewById(R.id.et_diary_content);
        btnSave = findViewById(R.id.btn_save_diary);
        btnCancel = findViewById(R.id.btn_cancel_diary);
    }

    private void populateData() {
        etTitle.setText(entry.getTitle());
        etContent.setText(entry.getContent());
    }

    private void setupListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.setTitle(etTitle.getText().toString().trim());
                entry.setContent(etContent.getText().toString().trim());
                entry.setTimestamp(System.currentTimeMillis());

                listener.onDiaryEdited(entry);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}