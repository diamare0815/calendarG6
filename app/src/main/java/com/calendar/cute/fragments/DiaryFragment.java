package com.calendar.cute.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.adapters.DiaryAdapter;
import com.calendar.cute.database.entities.DiaryEntity;
import com.calendar.cute.dialogs.AddDiaryDialog;
import com.calendar.cute.dialogs.EditDiaryDialog;
import com.calendar.cute.viewmodel.DiaryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DiaryFragment extends Fragment {

    private RecyclerView recyclerViewDiary;
    private FloatingActionButton fabAddDiary;
    private DiaryAdapter diaryAdapter;
    private List<DiaryEntity> diaryList;
    private DiaryViewModel diaryViewModel;

    private TextView tvDominantIcon, tvDominantCount;
    private TextView tvStreakCount;
    private TextView tvTotalEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);

        initViews(view);
        setupRecyclerView();
        observeData();

        return view;
    }

    private void initViews(View view) {
        recyclerViewDiary = view.findViewById(R.id.recycler_diary);
        fabAddDiary = view.findViewById(R.id.fab_add_diary);

        tvDominantIcon = view.findViewById(R.id.tv_dominant_icon);
        tvDominantCount = view.findViewById(R.id.tv_dominant_count);
        tvStreakCount = view.findViewById(R.id.tv_streak_count);
        tvTotalEntries = view.findViewById(R.id.tv_total_entries);

        diaryList = new ArrayList<>();

        fabAddDiary.setOnClickListener(v -> showAddDiaryDialog());
    }

    private void setupRecyclerView() {
        diaryAdapter = new DiaryAdapter(diaryList, getContext(), new DiaryAdapter.OnDiaryItemListener() {
            @Override
            public void onDiaryClick(DiaryEntity entry) {
                showEditDiaryDialog(entry);
            }

            @Override
            public void onDiaryDelete(DiaryEntity entry) {
                showDeleteConfirmationDialog(entry);
            }
        });

        recyclerViewDiary.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiary.setAdapter(diaryAdapter);
    }

    private void observeData() {
        diaryViewModel.getAllDiaries().observe(getViewLifecycleOwner(), diaryEntities -> {
            diaryList.clear();
            diaryList.addAll(diaryEntities);
            diaryAdapter.notifyDataSetChanged();
            updateDashboardStats();
        });
    }

    private void updateDashboardStats() {
        if (diaryList == null) return;
        tvTotalEntries.setText(String.valueOf(diaryList.size()));
        calculateDominantMood();
        calculateStreak();
    }

    private void calculateDominantMood() {
        if (diaryList.isEmpty()) {
            tvDominantIcon.setText("😐");
            tvDominantCount.setText("0");
            return;
        }

        Map<String, Integer> moodCount = new HashMap<>();
        for (DiaryEntity entry : diaryList) {
            String mood = entry.getMood();
            moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
        }

        int maxCount = 0;
        for (int count : moodCount.values()) {
            if (count > maxCount) maxCount = count;
        }

        String dominantMood = "neutral";
        for (DiaryEntity entry : diaryList) {
            if (moodCount.get(entry.getMood()) == maxCount) {
                dominantMood = entry.getMood();
                break;
            }
        }

        tvDominantCount.setText(String.valueOf(maxCount));
        tvDominantIcon.setText(getMoodEmoji(dominantMood));
    }

    private void calculateStreak() {
        if (diaryList.isEmpty()) {
            tvStreakCount.setText("0");
            return;
        }

        Set<String> writtenDays = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        for (DiaryEntity entry : diaryList) {
            writtenDays.add(sdf.format(new Date(entry.getTimestamp())));
        }

        List<String> sortedDays = new ArrayList<>(writtenDays);
        Collections.sort(sortedDays, Collections.reverseOrder());

        if (sortedDays.isEmpty()) {
            tvStreakCount.setText("0");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        String today = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = sdf.format(calendar.getTime());

        String newestDay = sortedDays.get(0);

        if (!newestDay.equals(today) && !newestDay.equals(yesterday)) {
            tvStreakCount.setText("0");
            return;
        }

        int streak = 0;
        Calendar checkDate = Calendar.getInstance();

        if (!newestDay.equals(today)) {
            checkDate.add(Calendar.DAY_OF_YEAR, -1);
        }

        for (int i = 0; i < sortedDays.size(); i++) {
            String targetDate = sdf.format(checkDate.getTime());
            if (sortedDays.contains(targetDate)) {
                streak++;
                checkDate.add(Calendar.DAY_OF_YEAR, -1);
            } else {
                break;
            }
        }

        tvStreakCount.setText(String.valueOf(streak));
    }

    private String getMoodEmoji(String mood) {
        if (mood == null) return "😐";
        switch (mood) {
            case "happy": return "😊";
            case "sad": return "😢";
            case "excited": return "🤩";
            case "calm": return "😌";
            case "angry": return "😠";
            default: return "😐";
        }
    }

    private void showAddDiaryDialog() {
        AddDiaryDialog dialog = new AddDiaryDialog(getContext(), entry -> diaryViewModel.insert(entry));
        dialog.show();
    }

    private void showEditDiaryDialog(DiaryEntity entry) {
        EditDiaryDialog dialog = new EditDiaryDialog(getContext(), entry, editedEntry -> diaryViewModel.update(editedEntry));
        dialog.show();
    }

    private void showDeleteConfirmationDialog(DiaryEntity entry) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete diary")
                .setMessage("Are you sure you want to delete this diary?")
                .setPositiveButton("Delete", (dialog, which) -> diaryViewModel.delete(entry))
                .setNegativeButton("Cancel", null)
                .show();
    }
}