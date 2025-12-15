package com.calendar.cute.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.adapters.StatsAdapter;
import com.calendar.cute.models.Habit;
import java.util.List;

public class StatsDialog extends Dialog {

    private final List<Habit> habitList;

    public StatsDialog(@NonNull Context context, List<Habit> habitList) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        this.habitList = habitList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stats);

        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_stats);
        ImageButton btnClose = findViewById(R.id.btn_close_stats);

        setupRecyclerView(recyclerView);

        btnClose.setOnClickListener(v -> dismiss());
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new StatsAdapter(habitList, getContext()));
    }
}