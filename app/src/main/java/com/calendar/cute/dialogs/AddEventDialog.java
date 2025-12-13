package com.calendar.cute.dialogs;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView;
import android.app.AlertDialog;
import android.provider.Settings;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.calendar.cute.Notification.ReminderReceiver;
import com.calendar.cute.R;
import com.calendar.cute.models.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventDialog extends Dialog {

    private static final String TAG = "AddEventDialog";

    private EditText etTitle, etTimeStart, etTimeEnd;
    private Button btnSave, btnCancel;
    private View colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow;

    private String selectedDate;
    private String selectedColor = "#FFB6C1";
    private Event editEvent = null;

    private Switch switchReminder;
    private Spinner spinnerReminder;
    private TextView tvSelectedReminder;

    private int reminderMinutes = 15; // default 15 mins

    private OnEventAddedListener listener;

    // color helpers
    private View[] colorViews;
    private String[] colorHexes = {
            "#FFB6C1", // pink
            "#FFE4B5", // peach
            "#E0BBE4", // purple
            "#B0E0E6", // blue
            "#98D8C8", // green
            "#F0E68C"  // yellow
    };

    public interface OnEventAddedListener {
        void onEventAdded(Event event);
    }

    public AddEventDialog(@NonNull Context context, @Nullable String date, @Nullable OnEventAddedListener listener){
        super(context);
        this.selectedDate = date;
        this.listener = listener;
    }

    public AddEventDialog(@NonNull Context context, @Nullable Event event, @Nullable OnEventAddedListener listener){
        super(context);
        this.editEvent = event;
        this.selectedDate = event.getDate();
        this.selectedColor = event.getColor() != null ? event.getColor() : selectedColor;
        this.listener = listener;
        this.reminderMinutes = event.hasReminder() ? event.getReminderMinutes() : 15;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_event);

        Window window = getWindow();
        if(window != null){
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(android.view.Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
        }

        etTitle = findViewById(R.id.et_event_title);
        etTimeStart = findViewById(R.id.et_event_time);
        etTimeEnd = findViewById(R.id.et_event_end);
        btnSave = findViewById(R.id.btn_save_event);
        btnCancel = findViewById(R.id.btn_cancel_event);

        colorPink = findViewById(R.id.color_pink);
        colorPeach = findViewById(R.id.color_peach);
        colorPurple = findViewById(R.id.color_purple);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        switchReminder = findViewById(R.id.switch_reminder);
        spinnerReminder = findViewById(R.id.spinner_reminder_time);
        tvSelectedReminder = findViewById(R.id.tv_selected_reminder);

        // if editing existing event, populate fields
        if(editEvent != null){
            etTitle.setText(editEvent.getTitle());
            etTimeStart.setText(editEvent.getTimeStart());
            etTimeEnd.setText(editEvent.getTimeEnd());
            switchReminder.setChecked(editEvent.hasReminder());
            spinnerReminder.setVisibility(editEvent.hasReminder() ? View.VISIBLE : View.GONE);
            tvSelectedReminder.setVisibility(editEvent.hasReminder() ? View.VISIBLE : View.GONE);
        } else {
            spinnerReminder.setVisibility(View.GONE);
            tvSelectedReminder.setVisibility(View.GONE);
        }

        // TimePickers
        etTimeStart.setFocusable(false);
        etTimeStart.setClickable(true);
        etTimeStart.setOnClickListener(v -> showTimePicker(etTimeStart));

        etTimeEnd.setFocusable(false);
        etTimeEnd.setClickable(true);
        etTimeEnd.setOnClickListener(v -> showTimePicker(etTimeEnd));

        // Spinner reminder (including "At time of event")
        String[] reminderOptions = {
                "15 minutes before",
                "20 minutes before",
                "30 minutes before",
                "At time of event"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, reminderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminder.setAdapter(adapter);

        // If editing and had reminder, set selection
        if(editEvent != null && editEvent.hasReminder()){
            spinnerReminder.setSelection(reminderMinutesToIndex(editEvent.getReminderMinutes()));
        }

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spinnerReminder.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            tvSelectedReminder.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if(isChecked) updateReminderText();
            else tvSelectedReminder.setText("Reminder: None");
        });

        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch(position){
                    case 0: reminderMinutes=15; break;
                    case 1: reminderMinutes=20; break;
                    case 2: reminderMinutes=30; break;
                    case 3: reminderMinutes=0;  break; // at time of event
                    default: reminderMinutes=15; break;
                }
                updateReminderText();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        // Color selection setup
        colorViews = new View[]{ colorPink, colorPeach, colorPurple, colorBlue, colorGreen, colorYellow };

        for (int i = 0; i < colorViews.length; i++) {
            final int idx = i;
            View cv = colorViews[i];
            // set initial background
            cv.setBackground(createCircleDrawable(colorHexes[i], false));
            // ensure starting scale is 1
            cv.setScaleX(1f);
            cv.setScaleY(1f);
            // set clickable listener
            cv.setOnClickListener(v -> {
                selectedColor = colorHexes[idx];
                updateColorSelection(); // apply animation + visuals
            });
        }

        // If editing pick existing color
        if (editEvent != null && editEvent.getColor() != null) {
            selectedColor = editEvent.getColor();
        }
        // Ensure visuals reflect selectedColor
        updateColorSelection();

        btnSave.setOnClickListener(v -> handleSave());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void updateReminderText(){
        if (reminderMinutes == 0) {
            tvSelectedReminder.setText("Reminder: At event time");
        } else {
            tvSelectedReminder.setText("Reminder: " + reminderMinutes + " minutes before");
        }
    }

    private int reminderMinutesToIndex(int minutes){
        switch(minutes){
            case 15: return 0;
            case 20: return 1;
            case 30: return 2;
            case 0:  return 3;
            default: return 0;
        }
    }

    private void showTimePicker(EditText target){
        Calendar c = Calendar.getInstance();
        int h=c.get(Calendar.HOUR_OF_DAY);
        int m=c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String amPm = hourOfDay>=12?"PM":"AM";
            int hour12 = hourOfDay%12;
            if(hour12==0) hour12=12;
            target.setText(String.format(Locale.getDefault(),"%d:%02d %s", hour12, minute, amPm));
        },h,m,false);
        dialog.show();
    }

    private void handleSave(){
        String title = etTitle.getText().toString().trim();
        String start = etTimeStart.getText().toString().trim();
        String end = etTimeEnd.getText().toString().trim();

        if(TextUtils.isEmpty(title)){ Toast.makeText(getContext(),"Nhập tiêu đề",Toast.LENGTH_SHORT).show(); return; }
        if(TextUtils.isEmpty(start)){ Toast.makeText(getContext(),"Chọn thời gian bắt đầu",Toast.LENGTH_SHORT).show(); return; }
        if(TextUtils.isEmpty(end)){ Toast.makeText(getContext(),"Chọn thời gian kết thúc",Toast.LENGTH_SHORT).show(); return; }

        boolean hasReminder = switchReminder.isChecked();

        // Nếu là edit: nếu trước đó có reminder mà giờ tắt -> cancel old reminder
        if (editEvent != null && editEvent.hasReminder() && !hasReminder) {
            cancelReminder(getContext(), editEvent);
        }

        // Nếu là edit: cập nhật chính đối tượng editEvent (không tạo mới)
        if (editEvent != null) {
            // lưu các giá trị mới vào editEvent
            editEvent.setTitle(title);
            editEvent.setTimeStart(start);
            editEvent.setTimeEnd(end);
            editEvent.setDate(selectedDate);
            editEvent.setColor(selectedColor);
            editEvent.setHasReminder(hasReminder);
            editEvent.setReminderMinutes(hasReminder ? reminderMinutes : 0);

            // nếu trước đó đã có reminder và user vẫn muốn reminder (hoặc thay đổi thời gian) -> cancel cũ và đặt lại
            // (nếu bạn đã cancel trước ở trên khi tắt, ở đây xử lý khi thay đổi thời gian)
            if (hasReminder) {
                // cancel existing first to avoid duplicate alarms
                cancelReminder(getContext(), editEvent);
                scheduleReminder(getContext(), editEvent);
            }

            if(listener != null) listener.onEventAdded(editEvent); // trả về obj đã chỉnh sửa
        } else {
            // tạo mới event
            Event event = new Event(title,start,end,selectedDate,selectedColor);
            event.setHasReminder(hasReminder);
            event.setReminderMinutes(hasReminder?reminderMinutes:0);

            if(hasReminder) {
                scheduleReminder(getContext(), event);
            }

            if(listener != null) listener.onEventAdded(event);
        }

        dismiss();
    }


    /**
     * scheduleReminder - TEST mode:
     * Currently set to trigger after 30s for quick verification.
     * To use production parsing from selectedDate + timeStart, uncomment the parsing block and remove the test trigger.
     */
    private void scheduleReminder(Context context, Event event){
        try{
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (am == null) {
                Toast.makeText(context, "Không thể truy cập AlarmManager", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1) Kiểm tra permission để đặt exact alarms (Android S / API 31+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                boolean canExact = am.canScheduleExactAlarms();
                if (!canExact) {
                    // Không được phép đặt exact alarms -> thông báo cho người dùng
                    showExactAlarmSettingsPrompt(context, "Ứng dụng chưa được phép đặt nhắc nhở chính xác. Vui lòng bật 'Exact alarms' cho ứng dụng trong cài đặt để nhận nhắc đúng giờ.");
                    // vẫn có thể thử fallback (inexact) nếu muốn; ở đây ta dừng và yêu cầu user bật permission
                    return;
                }
            }

            // 2) (Tuỳ chọn) kiểm tra permission thông báo (Android 13+). Nếu chưa có, gợi ý bật để user thấy notification khi reminder tới
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    showNotificationSettingsPrompt(context, "Ứng dụng chưa được phép gửi thông báo. Vui lòng cấp quyền thông báo để nhận nhắc nhở.");
                    // Không dừng ở đây — bạn có thể vẫn đặt alarm, nhưng notification sẽ không hiển thị.
                    // return; // nếu bạn muốn dừng cho tới khi user cấp quyền thì uncomment.
                }
            }

            // 3) Tính thời điểm trigger (ở đây giữ TEST hoặc production tùy bạn)
            // === TEST MODE: trigger after 30s (bạn có thể đổi về parse thực) ===
            long trigger = System.currentTimeMillis() + 30_000L;

            // === PRODUCTION (nếu cần): uncomment và dùng block dưới để parse date+time
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.getDefault());
        Date d = sdf.parse(event.getDate() + " " + event.getTimeStart());
        if (d == null) {
            Toast.makeText(context, "Không thể xác định thời gian sự kiện", Toast.LENGTH_SHORT).show();
            return;
        }
        trigger = d.getTime();
        if (event.getReminderMinutes() > 0) trigger -= (long) event.getReminderMinutes() * 60 * 1000;
        if (trigger <= System.currentTimeMillis()) {
            Toast.makeText(context, "Thời gian nhắc nhở đã qua.", Toast.LENGTH_SHORT).show();
            return;
        }
        */

            Intent i = new Intent(context, ReminderReceiver.class);
            i.putExtra(ReminderReceiver.EXTRA_TITLE, event.getTitle());
            i.putExtra(ReminderReceiver.EXTRA_TIME, event.getTimeStart());

            int requestCode = Math.abs((event.getTitle() + event.getDate() + event.getTimeStart()).hashCode());

            PendingIntent pi = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    i,
                    PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
            );

            // 4) Thực hiện đặt alarm, bọc try/catch để bắt SecurityException
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, trigger, pi);
                }
                Toast.makeText(context, "Đã đặt nhắc nhở", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Reminder scheduled at: " + new Date(trigger).toString());
                // lưu vào prefs nếu cần (để reschedule trên boot)
                saveReminderToPrefs(context, event);
            } catch (SecurityException se) {
                // Lỗi do policy/permission hệ thống, thông báo và gợi ý người dùng
                Log.e(TAG, "SecurityException scheduling alarm: " + se.getMessage(), se);
                showErrorDialog(context, "Không thể đặt nhắc nhở chính xác do hạn chế hệ thống. Bạn có thể bật 'Exact alarms' trong cài đặt ứng dụng.");
            } catch (Exception ex) {
                Log.e(TAG, "Exception scheduling alarm", ex);
                showErrorDialog(context, "Lỗi khi đặt nhắc nhở: " + ex.getMessage());
            }

        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi đặt nhắc nhở",Toast.LENGTH_SHORT).show();
        }
    }

    // Helper: show dialog hướng dẫn bật Exact Alarms (Android S+)
    private void showExactAlarmSettingsPrompt(Context context, String message) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Cần quyền Exact Alarms");
            b.setMessage(message);
            b.setPositiveButton("Mở cài đặt", (dialog, which) -> {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        // Intent để yêu cầu SCHEDULE_EXACT_ALARM (nếu hệ thống hỗ trợ)
                        Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        // Khi sử dụng Intent này, hệ thống sẽ hiển thị trang quản lý exact alarms cho app
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                            return;
                        }
                    }
                    // Fallback: mở trang cài đặt App để user bật thủ công các quyền
                    Intent appSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    appSettings.setData(android.net.Uri.parse("package:" + context.getPackageName()));
                    if (context instanceof Activity) {
                        ((Activity) context).startActivity(appSettings);
                    } else {
                        appSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(appSettings);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Không thể mở trang cài đặt", Toast.LENGTH_SHORT).show();
                }
            });
            b.setNegativeButton("Huỷ", (d, w) -> d.dismiss());
            b.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: show dialog hướng dẫn bật Notification permission (Android 13+)
    private void showNotificationSettingsPrompt(Context context, String message) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Cần quyền gửi thông báo");
            b.setMessage(message);
            b.setPositiveButton("Mở cài đặt thông báo", (dialog, which) -> {
                try {
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // Mở trang settings thông báo cho app
                        intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                    } else {
                        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(android.net.Uri.parse("package:" + context.getPackageName()));
                    }
                    if (context instanceof Activity) {
                        ((Activity) context).startActivity(intent);
                    } else {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Không thể mở cài đặt thông báo", Toast.LENGTH_SHORT).show();
                }
            });
            b.setNegativeButton("Huỷ", (d, w) -> d.dismiss());
            b.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: general error dialog
    private void showErrorDialog(Context context, String message) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Lỗi đặt nhắc nhở");
            b.setMessage(message);
            b.setPositiveButton("OK", (d, w) -> d.dismiss());
            b.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private void cancelReminder(Context context, Event event) {
        try {
            Intent i = new Intent(context, ReminderReceiver.class);
            int requestCode = Math.abs((event.getTitle() + event.getDate() + event.getTimeStart()).hashCode());

            PendingIntent pi = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    i,
                    PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
            );

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (am != null) {
                am.cancel(pi);
                pi.cancel();
                Log.d(TAG, "Cancelled reminder for requestCode=" + requestCode);
            }

            // remove from storage
            removeReminderFromPrefs(context, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Color selection helpers ----------
    private void updateColorSelection() {
        for (int i = 0; i < colorViews.length; i++) {
            boolean isSelected = colorHexes[i].equalsIgnoreCase(selectedColor);

            // Update background stroke thickness
            colorViews[i].setBackground(createCircleDrawable(colorHexes[i], isSelected));

            // Animate scale to emphasize selected color
            if (isSelected) {
                // enlarge slightly with animation
                colorViews[i].animate().scaleX(1.18f).scaleY(1.18f).setDuration(140).start();
                // elevate to cast shadow (API 21+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    colorViews[i].setElevation(dpToPx(6));
                }
            } else {
                // reset to normal size
                colorViews[i].animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    colorViews[i].setElevation(0);
                }
            }
        }
    }

    private GradientDrawable createCircleDrawable(String colorHex, boolean selected) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        try {
            d.setColor(Color.parseColor(colorHex));
        } catch (IllegalArgumentException ex) {
            d.setColor(Color.LTGRAY);
        }

        if (selected) {
            int strokePx = dpToPx(3);
            int strokeColor = isColorDark(colorHex) ? Color.WHITE : Color.BLACK;
            d.setStroke(strokePx, strokeColor);
        } else {
            d.setStroke(0, Color.TRANSPARENT);
        }
        return d;
    }

    private boolean isColorDark(String colorHex) {
        try {
            int color = Color.parseColor(colorHex);
            double r = Color.red(color) / 255.0;
            double g = Color.green(color) / 255.0;
            double b = Color.blue(color) / 255.0;
            double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
            return luminance < 0.5;
        } catch (Exception e) {
            return false;
        }
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // ---------- Persistence helpers (SharedPreferences) ----------
    private void saveReminderToPrefs(Context context, Event event) {
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences("reminders_prefs", Context.MODE_PRIVATE);
            String prev = prefs.getString("reminders", null);
            JSONArray arr = prev != null ? new JSONArray(prev) : new JSONArray();

            // create json object
            JSONObject obj = new JSONObject();
            obj.put("title", event.getTitle());
            obj.put("date", event.getDate());
            obj.put("timeStart", event.getTimeStart());
            obj.put("reminderMinutes", event.getReminderMinutes());
            obj.put("color", event.getColor());

            // Avoid duplicates: remove any existing with same keyId
            String keyId = "" + Math.abs((event.getTitle() + event.getDate() + event.getTimeStart()).hashCode());
            JSONArray newArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String t = o.optString("title") + o.optString("date") + o.optString("timeStart");
                if (!keyId.equals(String.valueOf(Math.abs(t.hashCode())))) {
                    newArr.put(o);
                }
            }
            newArr.put(obj);

            prefs.edit().putString("reminders", newArr.toString()).apply();
            Log.d(TAG, "Saved reminder to prefs: " + obj.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error saving reminder to prefs", e);
        }
    }

    private void removeReminderFromPrefs(Context context, Event event) {
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences("reminders_prefs", Context.MODE_PRIVATE);
            String prev = prefs.getString("reminders", null);
            if (prev == null) return;
            JSONArray arr = new JSONArray(prev);
            JSONArray newArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String title = o.optString("title");
                String date = o.optString("date");
                String timeStart = o.optString("timeStart");
                if (!(title.equals(event.getTitle()) && date.equals(event.getDate()) && timeStart.equals(event.getTimeStart()))) {
                    newArr.put(o);
                }
            }
            prefs.edit().putString("reminders", newArr.toString()).apply();
            Log.d(TAG, "Removed reminder from prefs for event: " + event.getTitle());
        } catch (Exception e) {
            Log.e(TAG, "Error removing reminder from prefs", e);
        }
    }

    // ---------- End persistence helpers ----------

    public void setOnEventAddedListener(OnEventAddedListener listener){ this.listener = listener; }
}
