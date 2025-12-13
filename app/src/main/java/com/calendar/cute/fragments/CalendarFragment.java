package com.calendar.cute.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.adapters.DayAdapter;
import com.calendar.cute.adapters.EventAdapter;
import com.calendar.cute.adapters.WeekAdapter;
import com.calendar.cute.dialogs.AddEventDialog;
import com.calendar.cute.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private TextView tvSelectedDate;
    private RecyclerView recyclerEvents, rvDay, rvWeek;
    private FloatingActionButton fabAddEvent;
    private EventAdapter eventAdapter;
    private DayAdapter dayAdapter;
    private WeekAdapter weekAdapter;
    private List<Event> eventList;
    private String selectedDate;

    private Button btnDayView, btnWeekView, btnMonthView;
    private ImageButton btnPrev, btnNext;
    private View llWeekNav;

    private List<LocalDate> weekDays;

    // Keep a consistent date format for display (you may consider storing ISO "yyyy-MM-dd" internally later)
    private final SimpleDateFormat fullFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
    private final DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.getDefault());

    // Permission request code for notifications (if needed)
    private static final int REQ_POST_NOTIFICATIONS = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initViews(view);
        setupCalendar();
        setupRecyclerViews();
        setupWeekRecyclerView();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        recyclerEvents = view.findViewById(R.id.recycler_events);
        rvDay = view.findViewById(R.id.recycler_day_view);
        rvWeek = view.findViewById(R.id.rv_week_days);
        fabAddEvent = view.findViewById(R.id.fab_add_event);

        btnDayView = view.findViewById(R.id.btn_day_view);
        btnWeekView = view.findViewById(R.id.btn_week_view);
        btnMonthView = view.findViewById(R.id.btn_month_view);
        btnPrev = view.findViewById(R.id.btn_prev_week);
        btnNext = view.findViewById(R.id.btn_next_week);
        llWeekNav = view.findViewById(R.id.ll_week_nav);

        eventList = new ArrayList<>();
        weekDays = new ArrayList<>();

        // Initialize selectedDate as today (formatted)
        selectedDate = fullFormat.format(new Date());
        tvSelectedDate.setText(selectedDate);
    }

    private void setupCalendar() {
        if (calendarView == null) return;

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // CalendarView month is 0-based (January = 0)
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            selectedDate = fullFormat.format(cal.getTime());
            tvSelectedDate.setText(selectedDate);

            loadEventsForDate(selectedDate);

            if (llWeekNav.getVisibility() == View.VISIBLE && weekAdapter != null) {
                updateWeekForCenterDate(cal);
            }

            if (rvDay.getVisibility() == View.VISIBLE && dayAdapter != null) {
                dayAdapter.updateDate(selectedDate);
            }
        });
    }

    private void setupRecyclerViews() {
        // Use requireContext() to avoid null context
        eventAdapter = new EventAdapter(new ArrayList<>(), requireContext());
        recyclerEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerEvents.setAdapter(eventAdapter);

        // Handle edit/delete via adapter callbacks
        eventAdapter.setOnEventActionListener(new EventAdapter.OnEventActionListener() {
            @Override
            public void onEdit(Event event, int position) {
                // Use requireContext() to create dialog
                AddEventDialog dialog = new AddEventDialog(requireContext(), event, updatedEvent -> {
                    // replace event at the same position
                    if (position >= 0 && position < eventList.size()) {
                        eventList.set(position, updatedEvent);
                    }
                    loadEventsForDate(selectedDate);
                    refreshWeekView();
                });
                dialog.show();
            }

            @Override
            public void onDelete(Event event, int position) {
                // Cancel any reminder if needed (if you implement cancel logic)
                // remove from list and refresh
                if (position >= 0 && position < eventList.size()) {
                    eventList.remove(position);
                } else {
                    // fallback: try remove by object
                    eventList.remove(event);
                }
                loadEventsForDate(selectedDate);
                refreshWeekView();
            }
        });

        // Day RecyclerView (vertical)
        dayAdapter = new DayAdapter(requireContext(), new DayAdapter.OnDayChangeListener() {
            @Override
            public void onChangeDay(String newDate) {
                selectedDate = newDate;
                tvSelectedDate.setText(selectedDate);
                loadEventsForDate(selectedDate);
                if (llWeekNav.getVisibility() == View.VISIBLE) {
                    try {
                        Calendar cal = Calendar.getInstance();
                        Date d = fullFormat.parse(selectedDate);
                        if (d != null) cal.setTime(d);
                        updateWeekForCenterDate(cal);
                    } catch (Exception ignored) {}
                }
            }
        });
        rvDay.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvDay.setAdapter(dayAdapter);

        // Load sample events and display
        loadSampleEvents();
        loadEventsForDate(selectedDate);
    }

    private void setupWeekRecyclerView() {
        rvWeek.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        weekAdapter = new WeekAdapter(weekDays, date -> {
            selectedDate = date.format(weekFormatter);
            tvSelectedDate.setText(selectedDate);
            loadEventsForDate(selectedDate);
            if (rvDay.getVisibility() == View.VISIBLE) dayAdapter.updateDate(selectedDate);
        });
        rvWeek.setAdapter(weekAdapter);
        llWeekNav.setVisibility(View.GONE);
    }

    private void setupButtons() {
        fabAddEvent.setOnClickListener(v -> showAddEventDialog());

        btnDayView.setOnClickListener(v -> switchToDayView());
        btnWeekView.setOnClickListener(v -> switchToWeekView());
        btnMonthView.setOnClickListener(v -> switchToMonthView());

        btnPrev.setOnClickListener(v -> {
            if (rvDay.getVisibility() == View.VISIBLE) shiftDayBy(-1);
            else shiftWeekBy(-1);
        });
        btnNext.setOnClickListener(v -> {
            if (rvDay.getVisibility() == View.VISIBLE) shiftDayBy(1);
            else shiftWeekBy(1);
        });

        // Default to month view
        switchToMonthView();
    }

    private void loadEventsForDate(String date) {
        List<Event> filtered = new ArrayList<>();
        for (Event e : eventList) {
            if (e.getDate() != null && e.getDate().equals(date)) filtered.add(e);
        }
        eventAdapter.updateEvents(filtered);
        if (rvDay.getVisibility() == View.VISIBLE) dayAdapter.updateEvents(filtered);
    }

    private void loadSampleEvents() {
        String today = fullFormat.format(new Date());
        Calendar cal = Calendar.getInstance();

        // Add sample events (use same date format as selectedDate)
        eventList.add(new Event("Team Meeting", "10:00 AM", "11:00 AM", today, "#FFB6C1"));
        eventList.add(new Event("Lunch with Sarah", "12:30 PM", "01:30 PM", today, "#FFE4B5"));
        eventList.add(new Event("Gym Session", "06:00 PM", "07:00 PM", today, "#E0BBE4"));

        cal.add(Calendar.DAY_OF_MONTH, 1);
        eventList.add(new Event("Project Review", "09:00 AM", "10:00 AM", fullFormat.format(cal.getTime()), "#C3FDB8"));

        cal.add(Calendar.DAY_OF_MONTH, -2);
        eventList.add(new Event("Doctor Appointment", "03:00 PM", "04:00 PM", fullFormat.format(cal.getTime()), "#FFD1DC"));

        eventAdapter.notifyDataSetChanged();
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog(requireContext(), selectedDate, event -> {
            eventList.add(event); // event has timeStart + timeEnd
            loadEventsForDate(selectedDate);
            refreshWeekView();
        });
        dialog.show();
    }

    private void switchToDayView() {
        btnDayView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_primary));
        btnWeekView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        btnMonthView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        calendarView.setVisibility(View.GONE);
        llWeekNav.setVisibility(View.GONE);
        rvDay.setVisibility(View.VISIBLE);
        recyclerEvents.setVisibility(View.VISIBLE);

        dayAdapter.updateDate(selectedDate);
        loadEventsForDate(selectedDate);
    }

    private void switchToWeekView() {
        btnWeekView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_primary));
        btnDayView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        btnMonthView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        calendarView.setVisibility(View.GONE);
        rvDay.setVisibility(View.GONE);
        recyclerEvents.setVisibility(View.VISIBLE);
        llWeekNav.setVisibility(View.VISIBLE);

        Calendar cal = Calendar.getInstance();
        try {
            Date d = fullFormat.parse(selectedDate);
            if (d != null) cal.setTime(d);
        } catch (Exception ignored) {}
        updateWeekForCenterDate(cal);
    }

    private void switchToMonthView() {
        btnMonthView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_primary));
        btnDayView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        btnWeekView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        calendarView.setVisibility(View.VISIBLE);
        rvDay.setVisibility(View.GONE);
        recyclerEvents.setVisibility(View.VISIBLE);
        llWeekNav.setVisibility(View.GONE);

        try {
            Date d = fullFormat.parse(selectedDate);
            if (d != null) calendarView.setDate(d.getTime(), false, true);
        } catch (Exception ignored) {}

        loadEventsForDate(selectedDate);
    }

    private void shiftDayBy(int deltaDays) {
        Calendar cal = Calendar.getInstance();
        try { Date d = fullFormat.parse(selectedDate); if(d!=null) cal.setTime(d); } catch(Exception ignored){}
        cal.add(Calendar.DAY_OF_MONTH, deltaDays);
        selectedDate = fullFormat.format(cal.getTime());
        tvSelectedDate.setText(selectedDate);

        dayAdapter.updateDate(selectedDate);
        loadEventsForDate(selectedDate);
    }

    private void shiftWeekBy(int deltaWeeks) {
        Calendar cal = Calendar.getInstance();
        try { Date d = fullFormat.parse(selectedDate); if(d!=null) cal.setTime(d); } catch(Exception ignored){}
        cal.add(Calendar.WEEK_OF_YEAR, deltaWeeks);
        updateWeekForCenterDate(cal);
    }

    // -------------------- Week Helper --------------------
    private void updateWeekForCenterDate(Calendar centerCal) {
        weekDays.clear();
        Calendar monday = (Calendar) centerCal.clone();
        int dow = monday.get(Calendar.DAY_OF_WEEK);
        // calculate how many days to go back to reach Monday
        int daysBack = (dow == Calendar.SUNDAY) ? 6 : (dow - Calendar.MONDAY);
        monday.add(Calendar.DAY_OF_MONTH, -daysBack);

        for (int i = 0; i < 7; i++) {
            Calendar d = (Calendar) monday.clone();
            d.add(Calendar.DAY_OF_MONTH, i);
            LocalDate ld = LocalDate.of(d.get(Calendar.YEAR), d.get(Calendar.MONTH)+1, d.get(Calendar.DAY_OF_MONTH));
            weekDays.add(ld);
        }

        // Filter events in the week
        List<Event> weekEvents = new ArrayList<>();
        for (Event e : eventList) {
            for (LocalDate ld : weekDays) {
                String dayStr = ld.format(weekFormatter);
                if (e.getDate() != null && e.getDate().equals(dayStr)) {
                    weekEvents.add(e);
                }
            }
        }

        weekAdapter.updateDays(weekDays);
        weekAdapter.setEvents(weekEvents);

        LocalDate selDate = LocalDate.of(centerCal.get(Calendar.YEAR), centerCal.get(Calendar.MONTH)+1,
                centerCal.get(Calendar.DAY_OF_MONTH));
        weekAdapter.selectDate(selDate);

        selectedDate = selDate.format(weekFormatter);
        tvSelectedDate.setText(selectedDate);

        loadEventsForDate(selectedDate);
    }

    private void refreshWeekView() {
        try {
            Calendar cal = Calendar.getInstance();
            Date d = fullFormat.parse(selectedDate);
            if (d != null) cal.setTime(d);
            updateWeekForCenterDate(cal);
        } catch (Exception ignored) {}
    }

    // Helper to show a local notification (used for testing)
    private void showEventNotification(String title, String time) {
        Context context = requireContext();

        // Create NotificationChannel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "event_channel",
                    "Event Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for event reminders");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "event_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Event Reminder")
                .setContentText(title + " at " + time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Check permission Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission from fragment (Fragment.requestPermissions)
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQ_POST_NOTIFICATIONS);
                return; // exit until permission granted
            }
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify((int) System.currentTimeMillis(), builder.build());
    }
}
