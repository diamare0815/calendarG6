package com.calendar.cute.fragments;
import com.calendar.cute.R;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.adapters.EventAdapter;
import com.calendar.cute.dialogs.AddEventDialog;
import com.calendar.cute.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private TextView tvSelectedDate;
    private RecyclerView recyclerViewEvents;
    private FloatingActionButton fabAddEvent;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private String selectedDate;
    private Button btnDayView, btnWeekView, btnMonthView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initViews(view);
        setupCalendar();
        setupRecyclerView();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        recyclerViewEvents = view.findViewById(R.id.recycler_events);
        fabAddEvent = view.findViewById(R.id.fab_add_event);
        btnDayView = view.findViewById(R.id.btn_day_view);
        btnWeekView = view.findViewById(R.id.btn_week_view);
        btnMonthView = view.findViewById(R.id.btn_month_view);

        eventList = new ArrayList<>();

        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        tvSelectedDate.setText(selectedDate);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
                selectedDate = sdf.format(calendar.getTime());
                tvSelectedDate.setText(selectedDate);

                // Load events for selected date
                loadEventsForDate(selectedDate);
            }
        });
    }

    private void setupRecyclerView() {
        eventAdapter = new EventAdapter(eventList, getContext());
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEvents.setAdapter(eventAdapter);

        // Load sample events
        loadSampleEvents();
    }

    private void setupButtons() {
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventDialog();
            }
        });

        btnDayView.setOnClickListener(v -> switchToDayView());
        btnWeekView.setOnClickListener(v -> switchToWeekView());
        btnMonthView.setOnClickListener(v -> switchToMonthView());
    }

    private void loadEventsForDate(String date) {
        // Filter events by date
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getDate().equals(date)) {
                filteredEvents.add(event);
            }
        }
        eventAdapter.updateEvents(filteredEvents);
    }

    private void loadSampleEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String today = sdf.format(new Date());

        eventList.add(new Event("Team Meeting", "10:00 AM", today, "#FFB6C1"));
        eventList.add(new Event("Lunch with Sarah", "12:30 PM", today, "#FFE4B5"));
        eventList.add(new Event("Gym Session", "6:00 PM", today, "#E0BBE4"));

        eventAdapter.notifyDataSetChanged();
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog(getContext(), selectedDate, new AddEventDialog.OnEventAddedListener() {
            @Override
            public void onEventAdded(Event event) {
                eventList.add(event);
                loadEventsForDate(selectedDate);
            }
        });
        dialog.show();
    }

    private void switchToDayView() {
        btnDayView.setBackgroundColor(getResources().getColor(R.color.pink_primary));
        btnWeekView.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnMonthView.setBackgroundColor(getResources().getColor(android.R.color.white));
        // Implement day view logic
    }

    private void switchToWeekView() {
        btnWeekView.setBackgroundColor(getResources().getColor(R.color.pink_primary));
        btnDayView.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnMonthView.setBackgroundColor(getResources().getColor(android.R.color.white));
        // Implement week view logic
    }

    private void switchToMonthView() {
        btnMonthView.setBackgroundColor(getResources().getColor(R.color.pink_primary));
        btnDayView.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnWeekView.setBackgroundColor(getResources().getColor(android.R.color.white));
        // Implement month view logic
    }
}