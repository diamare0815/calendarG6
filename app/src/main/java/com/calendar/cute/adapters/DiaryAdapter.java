package com.calendar.cute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.database.entities.DiaryEntity;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<DiaryEntity> diaryList;
    private final Context context;
    private final OnDiaryItemListener listener;

    public interface OnDiaryItemListener {
        void onDiaryClick(DiaryEntity entry);
        void onDiaryDelete(DiaryEntity entry);
    }

    public DiaryAdapter(List<DiaryEntity> diaryList, Context context, OnDiaryItemListener listener) {
        this.diaryList = diaryList;
        this.context = context;
        this.listener = listener;
    }

    public void updateData(List<DiaryEntity> newList) {
        this.diaryList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        DiaryEntity entry = diaryList.get(position);

        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvDate.setText(entry.getDate());
        holder.tvMoodIcon.setText(getMoodEmoji(entry.getMood()));

        bindEvents(holder, entry);
    }

    @Override
    public int getItemCount() {
        return diaryList != null ? diaryList.size() : 0;
    }

    private void bindEvents(DiaryViewHolder holder, DiaryEntity entry) {
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDiaryClick(entry);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onDiaryDelete(entry);
            return true;
        });
    }

    private String getMoodEmoji(String mood) {
        if (mood == null) return "😐";
        switch (mood.toLowerCase()) {
            case "happy": return "😊";
            case "sad": return "😢";
            case "excited": return "🤩";
            case "calm": return "😌";
            case "angry": return "😠";
            default: return "😐";
        }
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate, tvMoodIcon;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_diary_title);
            tvContent = itemView.findViewById(R.id.tv_diary_content);
            tvDate = itemView.findViewById(R.id.tv_diary_date);
            tvMoodIcon = itemView.findViewById(R.id.tv_diary_mood);
        }
    }
}