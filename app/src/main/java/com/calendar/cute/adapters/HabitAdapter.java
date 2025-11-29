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

import com.calendar.cute.models.Habit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.calendar.cute.R;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList;
    private Context context;
    private OnHabitItemListener listener;

    public interface OnHabitItemListener {
        void onHabitCheck(Habit habit, boolean isChecked);
        void onHabitDelete(Habit habit);
        void onHabitEdit(Habit habit);
    }

    public HabitAdapter(List<Habit> habitList, Context context, OnHabitItemListener listener) {
        this.habitList = habitList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        holder.tvName.setText(habit.getName());
        holder.tvIcon.setText(habit.getIcon());
        holder.tvStreak.setText(habit.getCurrentStreak() + " / " + habit.getGoalDays() + " days");

        int progress = habit.getProgress();
        holder.progressBar.setProgress(progress);
        holder.tvProgress.setText(progress + "%");

        try {
            holder.cardView.setCardBackgroundColor(Color.parseColor(habit.getColor()));
        } catch (IllegalArgumentException e) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFB6C1"));
        }

        holder.btnCheck.setOnClickListener(v -> {
            listener.onHabitCheck(habit, true);
        });

        holder.cardView.setOnClickListener(v -> listener.onHabitEdit(habit));
        holder.cardView.setOnLongClickListener(v -> {
            listener.onHabitDelete(habit);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvIcon, tvStreak, tvProgress;
        android.widget.ProgressBar progressBar;
        android.widget.Button btnCheck;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_habit);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvIcon = itemView.findViewById(R.id.tv_habit_icon);
            tvStreak = itemView.findViewById(R.id.tv_habit_streak);
            tvProgress = itemView.findViewById(R.id.tv_habit_progress);
            progressBar = itemView.findViewById(R.id.progress_habit);
            btnCheck = itemView.findViewById(R.id.btn_check_habit);
        }
    }
}