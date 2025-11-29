package com.calendar.cute.adapters;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.models.DiaryEntry;
import com.calendar.cute.R;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<DiaryEntry> diaryList;
    private Context context;
    private OnDiaryItemListener listener;

    public interface OnDiaryItemListener {
        void onDiaryClick(DiaryEntry entry);
        void onDiaryDelete(DiaryEntry entry);
    }

    public DiaryAdapter(List<DiaryEntry> diaryList, Context context, OnDiaryItemListener listener) {
        this.diaryList = diaryList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        DiaryEntry entry = diaryList.get(position);

        holder.tvTitle.setText(entry.getTitle());
        holder.tvContent.setText(entry.getContent());
        holder.tvDate.setText(entry.getDate());

        // Set mood emoji
        String moodEmoji = getMoodEmoji(entry.getMood());
        holder.tvMood.setText(moodEmoji);

        holder.cardView.setOnClickListener(v -> listener.onDiaryClick(entry));
        holder.cardView.setOnLongClickListener(v -> {
            listener.onDiaryDelete(entry);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    private String getMoodEmoji(String mood) {
        switch (mood) {
            case "happy": return "😊";
            case "sad": return "😢";
            case "excited": return "🤩";
            case "calm": return "😌";
            case "angry": return "😠";
            default: return "😐";
        }
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvContent, tvDate, tvMood;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_diary);
            tvTitle = itemView.findViewById(R.id.tv_diary_title);
            tvContent = itemView.findViewById(R.id.tv_diary_content);
            tvDate = itemView.findViewById(R.id.tv_diary_date);
            tvMood = itemView.findViewById(R.id.tv_diary_mood);
        }
    }
}
