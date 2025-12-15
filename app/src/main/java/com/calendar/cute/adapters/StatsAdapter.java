package com.calendar.cute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.calendar.cute.R;
import com.calendar.cute.models.Habit;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private List<Habit> habitList;
    private final Context context;

    public StatsAdapter(List<Habit> habitList, Context context) {
        this.habitList = habitList;
        this.context = context;
    }

    public void updateData(List<Habit> newList) {
        this.habitList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stat, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        holder.tvIcon.setText(habit.getIcon());
        holder.tvName.setText(getFormattedHabitText(habit));
        holder.tvMessage.setText(getMotivation(habit));

        setupLineChart(holder.chart, habit);
    }

    @Override
    public int getItemCount() {
        return habitList != null ? habitList.size() : 0;
    }

    private CharSequence getFormattedHabitText(Habit habit) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String name = habit.getName();
        SpannableString nameSpannable = new SpannableString(name);
        nameSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
        builder.append(nameSpannable);

        builder.append("\n");

        String rawDate = habit.getStartDate();
        String dateString = (rawDate != null) ? rawDate : "Unknown";
        String dateText = "(Started: " + dateString + ")";

        SpannableString dateSpannable = new SpannableString(dateText);
        dateSpannable.setSpan(new StyleSpan(Typeface.NORMAL), 0, dateText.length(), 0);
        dateSpannable.setSpan(new RelativeSizeSpan(0.85f), 0, dateText.length(), 0);
        dateSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, dateText.length(), 0);

        builder.append(dateSpannable);

        return builder;
    }

    private String getMotivation(Habit habit) {
        int progress = habit.getProgress();
        if (habit.isCompletedToday()) return "Excellent! Task completed for today ✔️";
        if (progress == 0) return "Every big journey begins with a small step. Let's start! 🚀";
        if (progress <= 20) return "Great start! Keep this momentum going 🌱";
        if (progress <= 50) return "You are almost halfway there! Don't give up 🔥";
        if (progress <= 80) return "Very consistent! You are getting closer to your goal 💪";
        return "Outstanding! You have conquered this habit 🏆";
    }



    private void setupLineChart(LineChart chart, Habit habit) {
        int color;
        try {
            color = Color.parseColor(habit.getColor());
        } catch (Exception e) {
            color = Color.parseColor("#FF69B4");
        }

        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> xLabels = new ArrayList<>();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        java.text.SimpleDateFormat sdfLabel = new java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault());

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        try {
            if (habit.getStartDate() != null) {
                calendar.setTime(sdf.parse(habit.getStartDate()));
            } else {
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -7);
            }
        } catch (Exception e) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -7);
        }

        java.util.Calendar endCalendar = java.util.Calendar.getInstance();
        endCalendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(java.util.Calendar.MINUTE, 59);

        int index = 0;
        float totalDoneCount = 0;

        while (calendar.before(endCalendar)) {
            String dateKey = sdf.format(calendar.getTime());
            xLabels.add(sdfLabel.format(calendar.getTime()));

            if (habit.isCompletedOnDate(dateKey)) {
                totalDoneCount += 1;
            }

            entries.add(new Entry(index, totalDoneCount));

            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
            index++;
        }

        LineDataSet set = new LineDataSet(entries, "Progress");
        set.setMode(LineDataSet.Mode.STEPPED);
        set.setDrawFilled(true);
        set.setFillColor(color);
        set.setColor(color);
        set.setCircleColor(color);
        set.setDrawValues(false);
        set.setLineWidth(2f);

        if (index > 20) {
            set.setDrawCircles(false);
        } else {
            set.setCircleRadius(3f);
            set.setCircleHoleRadius(1.5f);
        }

        LineData data = new LineData(set);
        chart.setData(data);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setLabelCount(5, false);
        xAxis.setGranularity(1f);

        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        float goal = (float) habit.getGoalDays();
        leftAxis.setAxisMaximum(Math.max(totalDoneCount, goal) + 5f);

        LimitLine goalLine = new LimitLine(goal, "Goal: " + (int)goal);
        goalLine.setLineWidth(2f);
        goalLine.enableDashedLine(10f, 10f, 0f);
        goalLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        goalLine.setLineColor(Color.RED);
        goalLine.setTextColor(Color.RED);

        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(goalLine);

        chart.setExtraBottomOffset(10f);

        chart.setTouchEnabled(false);
        chart.invalidate();
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName, tvMessage;
        LineChart chart;

        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tv_stat_icon);
            tvName = itemView.findViewById(R.id.tv_stat_name);
            tvMessage = itemView.findViewById(R.id.tv_smart_message);
            chart = itemView.findViewById(R.id.chart_habit);
        }
    }
}