package com.calendar.cute.fragments;
import com.calendar.cute.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.adapters.DiaryAdapter;
import com.calendar.cute.dialogs.AddDiaryDialog;
import com.calendar.cute.dialogs.EditDiaryDialog;
import com.calendar.cute.models.DiaryEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryFragment extends Fragment {

    private RecyclerView recyclerViewDiary;
    private FloatingActionButton fabAddDiary;
    private DiaryAdapter diaryAdapter;
    private List<DiaryEntry> diaryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        initViews(view);
        setupRecyclerView();

        return view;
    }

    private void initViews(View view) {
        recyclerViewDiary = view.findViewById(R.id.recycler_diary);
        fabAddDiary = view.findViewById(R.id.fab_add_diary);

        diaryList = new ArrayList<>();

        fabAddDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDiaryDialog();
            }
        });
    }

    private void setupRecyclerView() {
        diaryAdapter = new DiaryAdapter(diaryList, getContext(), new DiaryAdapter.OnDiaryItemListener() {
            @Override
            public void onDiaryClick(DiaryEntry entry) {
                showEditDiaryDialog(entry);
            }

            @Override
            public void onDiaryDelete(DiaryEntry entry) {
                diaryList.remove(entry);
                diaryAdapter.notifyDataSetChanged();
            }
        });

        recyclerViewDiary.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiary.setAdapter(diaryAdapter);

        loadSampleDiaries();
    }

    private void loadSampleDiaries() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        long today = System.currentTimeMillis();

        diaryList.add(new DiaryEntry(
                "Amazing Day!",
                "Today was incredibly productive. Finished all my tasks and went for a run in the evening. Feeling grateful!",
                "happy",
                sdf.format(new Date(today)),
                today
        ));

        diaryList.add(new DiaryEntry(
                "Quiet Sunday",
                "Spent the day reading and relaxing. Sometimes it's nice to just slow down and enjoy the moment.",
                "calm",
                sdf.format(new Date(today - 86400000)),
                today - 86400000
        ));

        diaryList.add(new DiaryEntry(
                "Challenging Meeting",
                "Had a tough presentation at work. It didn't go as planned but learned a lot from the experience.",
                "neutral",
                sdf.format(new Date(today - 172800000)),
                today - 172800000
        ));

        diaryAdapter.notifyDataSetChanged();
    }

    private void showAddDiaryDialog() {
        AddDiaryDialog dialog = new AddDiaryDialog(getContext(), new AddDiaryDialog.OnDiaryAddedListener() {
            @Override
            public void onDiaryAdded(DiaryEntry entry) {
                diaryList.add(0, entry);
                diaryAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void showEditDiaryDialog(DiaryEntry entry) {
        EditDiaryDialog dialog = new EditDiaryDialog(getContext(), entry, new EditDiaryDialog.OnDiaryEditedListener() {
            @Override
            public void onDiaryEdited(DiaryEntry editedEntry) {
                diaryAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}