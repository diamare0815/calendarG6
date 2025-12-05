package com.calendar.cute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayVH> {

    public interface OnDayChangeListener {
        void onChangeDay(String newDate);
    }

    public interface OnEventActionListener {
        void onEdit(Event event, int position);
        void onDelete(Event event, int position);
    }

    private Context context;
    private List<Event> events;
    private String currentDate;
    private OnDayChangeListener dayChangeListener;
    private OnEventActionListener eventActionListener;

    private final SimpleDateFormat fullFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());

    private EventAdapter eventAdapter; // giữ 1 adapter duy nhất

    public DayAdapter(Context context, OnDayChangeListener listener) {
        this.context = context;
        this.dayChangeListener = listener;
        this.events = new ArrayList<>();
        this.currentDate = fullFormat.format(new Date());

        // khởi tạo EventAdapter 1 lần
        this.eventAdapter = new EventAdapter(events, context);
        this.eventAdapter.setOnEventActionListener(new EventAdapter.OnEventActionListener() {
            @Override
            public void onEdit(Event event, int position) {
                if (eventActionListener != null) eventActionListener.onEdit(event, position);
            }

            @Override
            public void onDelete(Event event, int position) {
                if (eventActionListener != null) eventActionListener.onDelete(event, position);
            }
        });
    }

    public void setOnEventActionListener(OnEventActionListener listener) {
        this.eventActionListener = listener;
    }

    @NonNull
    @Override
    public DayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_day_cell, parent, false);
        DayVH vh = new DayVH(v);
        vh.rvEvents.setLayoutManager(new LinearLayoutManager(context));
        vh.rvEvents.setAdapter(eventAdapter); // gắn adapter 1 lần
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DayVH holder, int position) {
        Date dateObj = parseDate(currentDate);

        // Hiển thị ngày, thứ, tháng
        holder.tvDayNumber.setText(new SimpleDateFormat("d", Locale.getDefault()).format(dateObj));
        holder.tvWeekday.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateObj));
        holder.tvMonth.setText(new SimpleDateFormat("MMMM", Locale.getDefault()).format(dateObj));

        // Hiển thị ngôi sao nếu có sự kiện
        boolean hasEvent = false;
        for (Event e : events) {
            if (e.getDate() != null && e.getDate().equals(fullFormat.format(dateObj))) {
                hasEvent = true;
                break;
            }
        }
        holder.vEventDot.setVisibility(hasEvent ? View.VISIBLE : View.GONE);

        // prev/next day
        holder.btnPrev.setOnClickListener(v -> shiftDay(-1));
        holder.btnNext.setOnClickListener(v -> shiftDay(1));

        // cập nhật dữ liệu cho EventAdapter
        eventAdapter.updateEvents(events);
    }

    @Override
    public int getItemCount() {
        return 1; // Chỉ hiển thị 1 ngày
    }

    public void updateDate(String newDate) {
        this.currentDate = newDate;
        if (dayChangeListener != null) dayChangeListener.onChangeDay(currentDate);
        notifyDataSetChanged();
    }

    public void updateEvents(List<Event> newEvents) {
        this.events = newEvents != null ? newEvents : new ArrayList<>();
        eventAdapter.updateEvents(this.events);
        notifyDataSetChanged();
    }

    private void shiftDay(int delta) {
        try {
            Date d = fullFormat.parse(currentDate);
            if (d != null) {
                long millis = d.getTime() + delta * 24L * 60L * 60L * 1000L;
                currentDate = fullFormat.format(new Date(millis));
                if (dayChangeListener != null) dayChangeListener.onChangeDay(currentDate);
                notifyDataSetChanged();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return fullFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    static class DayVH extends RecyclerView.ViewHolder {
        TextView tvDayNumber, tvWeekday, tvMonth;
        RecyclerView rvEvents;
        ImageButton btnPrev, btnNext;
        ImageView vEventDot;

        DayVH(@NonNull View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
            tvWeekday = itemView.findViewById(R.id.tv_weekday);
            tvMonth = itemView.findViewById(R.id.tv_month);
            rvEvents = itemView.findViewById(R.id.rv_events);
            btnPrev = itemView.findViewById(R.id.btn_prev_day);
            btnNext = itemView.findViewById(R.id.btn_next_day);
            vEventDot = itemView.findViewById(R.id.v_event_dot);
        }
    }
}
