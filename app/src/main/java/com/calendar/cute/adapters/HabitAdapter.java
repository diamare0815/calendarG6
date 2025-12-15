package com.calendar.cute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.models.Habit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList;
    private final Context context;
    private final OnHabitItemListener listener;

    public interface OnHabitItemListener {
        void onHabitClick(Habit habit);
        void onHabitDelete(Habit habit);
        void onHabitEdit(Habit habit);
    }

    public HabitAdapter(List<Habit> habitList, Context context, OnHabitItemListener listener) {
        this.habitList = habitList;
        this.context = context;
        this.listener = listener;
    }

    public void updateData(List<Habit> newList) {
        this.habitList = newList;
        notifyDataSetChanged();
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

        int habitColor = getHabitColor(habit.getColor());
        holder.cardView.setCardBackgroundColor(habitColor);

        if (habit.isCompletedToday()) {
            holder.btnCheck.setAlpha(0.5f);
            holder.btnCheck.setText("✓");
        } else {
            holder.btnCheck.setAlpha(1.0f);
            holder.btnCheck.setText("");
        }

        setupWeeklyCalendar(holder, habit, habitColor);
        bindEvents(holder, habit);
    }

    @Override
    public int getItemCount() {
        return habitList != null ? habitList.size() : 0;
    }

    private void bindEvents(HabitViewHolder holder, Habit habit) {
        holder.cardView.setOnClickListener(v -> listener.onHabitEdit(habit));

        holder.cardView.setOnLongClickListener(v -> {
            listener.onHabitDelete(habit);
            return true;
        });

        holder.btnCheck.setOnClickListener(v -> listener.onHabitClick(habit));
    }

    private int getHabitColor(String colorString) {
        try {
            return Color.parseColor(colorString);
        } catch (Exception e) {
            return Color.parseColor("#FFB6C1");
        }
    }

    private void setupWeeklyCalendar(HabitViewHolder holder, Habit habit, int habitColor) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        int currentDow = cal.get(Calendar.DAY_OF_WEEK);
        int delta = (currentDow == Calendar.SUNDAY) ? -6 : Calendar.MONDAY - currentDow;
        cal.add(Calendar.DAY_OF_MONTH, delta);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        checkDay(habit, holder.tvMon, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvTue, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvWed, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvThu, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvFri, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvSat, cal, sdf, habitColor); cal.add(Calendar.DAY_OF_YEAR, 1);
        checkDay(habit, holder.tvSun, cal, sdf, habitColor);
    }

    private void checkDay(Habit habit, TextView tv, Calendar cal, SimpleDateFormat sdf, int activeColor) {
        String dateString = sdf.format(cal.getTime());
        if (habit.isCompletedOnDate(dateString)) {
            tv.setBackgroundResource(R.drawable.circle_bg_white);
            tv.setTextColor(activeColor);
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            tv.setBackgroundResource(R.drawable.circle_bg_transparent);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(null, Typeface.NORMAL);
        }
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvIcon, tvStreak, tvProgress;
        ProgressBar progressBar;
        Button btnCheck;
        TextView tvMon, tvTue, tvWed, tvThu, tvFri, tvSat, tvSun;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_habit);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvIcon = itemView.findViewById(R.id.tv_habit_icon);
            tvStreak = itemView.findViewById(R.id.tv_habit_streak);
            tvProgress = itemView.findViewById(R.id.tv_habit_progress);
            progressBar = itemView.findViewById(R.id.progress_habit);
            btnCheck = itemView.findViewById(R.id.btn_check_habit);

            tvMon = itemView.findViewById(R.id.tv_mon);
            tvTue = itemView.findViewById(R.id.tv_tue);
            tvWed = itemView.findViewById(R.id.tv_wed);
            tvThu = itemView.findViewById(R.id.tv_thu);
            tvFri = itemView.findViewById(R.id.tv_fri);
            tvSat = itemView.findViewById(R.id.tv_sat);
            tvSun = itemView.findViewById(R.id.tv_sun);
        }
    }
}