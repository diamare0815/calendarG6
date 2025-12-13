package com.calendar.cute.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.models.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayViewHolder> {

    private List<LocalDate> days;
    private List<Event> events = new ArrayList<>();
    private OnDayClickListener listener;
    private int selectedIndex = -1;

    private DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault());
    private DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.getDefault());
    private DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public interface OnDayClickListener {
        void onDayClick(LocalDate date);
    }

    public WeekAdapter(List<LocalDate> days, OnDayClickListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_day, parent, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        final LocalDate date = days.get(position);
        LocalDate today = LocalDate.now();
        boolean isToday = date.equals(today);

        // Hiển thị tên ngày trong tuần (Mon, Tue…)
        holder.tvWeekday.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        holder.tvDayNumber.setText(date.format(dayFormatter));

        // Check sự kiện: hiển thị sao nếu có event
        boolean hasEvent = false;
        for (Event e : events) {
            if (e == null || e.getDate() == null) continue;
            String edt = e.getDate();
            LocalDate eventDate = null;
            try {
                eventDate = LocalDate.parse(edt, displayFormatter);
            } catch (Exception ex1) {
                try {
                    eventDate = LocalDate.parse(edt, isoFormatter);
                } catch (Exception ignored) {}
            }
            if (eventDate != null && eventDate.equals(date)) {
                hasEvent = true;
                break;
            }
        }
        holder.vEventStar.setVisibility(hasEvent ? View.VISIBLE : View.GONE);

        // Reset style mặc định
        holder.tvDayNumber.setBackgroundResource(R.drawable.bg_day_normal);
        holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
        holder.tvWeekday.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
        holder.tvDayNumber.setTypeface(null, Typeface.NORMAL);

        // Highlight ngày chọn
        if (position == selectedIndex) {
            holder.tvDayNumber.setBackgroundResource(R.drawable.bg_day_selected);
            holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
            holder.tvWeekday.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
            holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
        } else if (isToday) {
            // Highlight ngày hôm nay (nếu chưa chọn)
            holder.tvDayNumber.setBackgroundResource(R.drawable.bg_today_fade);
            holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
            holder.tvWeekday.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
            holder.tvDayNumber.setTypeface(null, Typeface.NORMAL);
        }

        // Click chọn ngày
        holder.itemView.setOnClickListener(v -> {
            int prev = selectedIndex;
            selectedIndex = holder.getAdapterPosition();
            if (prev >= 0) notifyItemChanged(prev);
            notifyItemChanged(selectedIndex);
            if (listener != null) listener.onDayClick(date);
        });
    }

    @Override
    public int getItemCount() {
        return days != null ? days.size() : 0;
    }

    // Cập nhật danh sách ngày
    public void updateDays(List<LocalDate> newDays) {
        this.days = newDays;
        selectedIndex = -1;
        notifyDataSetChanged();
    }

    // Chọn ngày cụ thể
    public void selectDate(LocalDate date) {
        if (date == null || days == null) return;
        int idx = days.indexOf(date);
        if (idx >= 0) {
            int prev = selectedIndex;
            selectedIndex = idx;
            if (prev >= 0) notifyItemChanged(prev);
            notifyItemChanged(selectedIndex);
        }
    }

    // Cập nhật danh sách sự kiện
    public void setEvents(List<Event> events) {
        this.events = events != null ? events : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeekday;
        TextView tvDayNumber;
        ImageView vEventStar;

        DayViewHolder(View itemView) {
            super(itemView);
            tvWeekday = itemView.findViewById(R.id.tv_weekday_name);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
            vEventStar = itemView.findViewById(R.id.v_event_star);
        }
    }
}
