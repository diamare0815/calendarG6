package com.calendar.cute.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.calendar.cute.Notification.ReminderReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent == null ? "" : intent.getAction();
        Log.d(TAG, "onReceive action=" + action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) || "android.intent.action.QUICKBOOT_POWERON".equals(action)) {
            // Load reminders from SharedPreferences and reschedule
            try {
                android.content.SharedPreferences prefs = context.getSharedPreferences("reminders_prefs", Context.MODE_PRIVATE);
                String json = prefs.getString("reminders", null);
                if (json == null) {
                    Log.d(TAG, "No reminders to reschedule");
                    return;
                }

                JSONArray arr = new JSONArray(json);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.getDefault());

                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String title = obj.optString("title");
                    String date = obj.optString("date");
                    String timeStart = obj.optString("timeStart");
                    int reminderMinutes = obj.optInt("reminderMinutes", 0);

                    String combined = date + " " + timeStart;
                    Date d = null;
                    try { d = sdf.parse(combined); } catch (Exception pe) { Log.w(TAG, "Parse fail for " + combined, pe); }

                    long trigger = (d != null) ? d.getTime() : -1;
                    if (trigger != -1 && reminderMinutes > 0) trigger -= (long) reminderMinutes * 60 * 1000;

                    long now = System.currentTimeMillis();
                    if (trigger <= now) {
                        Log.d(TAG, "Skipping past reminder: " + title + " at " + combined);
                        continue;
                    }

                    Intent iRem = new Intent(context, ReminderReceiver.class);
                    iRem.putExtra(ReminderReceiver.EXTRA_TITLE, title);
                    iRem.putExtra(ReminderReceiver.EXTRA_TIME, timeStart);

                    int requestCode = Math.abs((title + date + timeStart).hashCode());
                    PendingIntent pi = PendingIntent.getBroadcast(
                            context,
                            requestCode,
                            iRem,
                            PendingIntent.FLAG_UPDATE_CURRENT | (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
                    );

                    if (am != null) {
                        // Safe scheduling: check canScheduleExactAlarms on Android S+ and handle SecurityException
                        try {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                boolean canExact = am.canScheduleExactAlarms();
                                if (!canExact) {
                                    // App isn't permitted to schedule exact alarms (S+). Use fallback inexact scheduling.
                                    Log.w(TAG, "App cannot schedule exact alarms (canScheduleExactAlarms=false). Using fallback scheduling for: " + title);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    } else {
                                        am.set(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    }
                                } else {
                                    // allowed to schedule exact alarms
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    } else {
                                        am.setExact(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    }
                                }
                            } else {
                                // Pre-S: try exact scheduling but catch SecurityException if thrown
                                try {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    } else {
                                        am.setExact(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    }
                                } catch (SecurityException se) {
                                    Log.w(TAG, "SecurityException when setting exact alarm (fallback to inexact): " + se.getMessage(), se);
                                    // fallback
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    } else {
                                        am.set(AlarmManager.RTC_WAKEUP, trigger, pi);
                                    }
                                }
                            }
                            Log.d(TAG, "Rescheduled reminder: " + title + " at " + new Date(trigger).toString());
                        } catch (SecurityException seOuter) {
                            // In case other security issues, catch and fallback
                            Log.e(TAG, "SecurityException scheduling alarm, falling back: " + seOuter.getMessage(), seOuter);
                            try {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi);
                                } else {
                                    am.set(AlarmManager.RTC_WAKEUP, trigger, pi);
                                }
                                Log.d(TAG, "Fallback scheduling succeeded for: " + title);
                            } catch (Exception fallbackEx) {
                                Log.e(TAG, "Fallback scheduling failed", fallbackEx);
                            }
                        } catch (Exception e2) {
                            Log.e(TAG, "Unexpected exception while scheduling alarm", e2);
                        }
                    } else {
                        Log.w(TAG, "AlarmManager is null, cannot schedule: " + title);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Error rescheduling reminders", e);
            }
        }
    }
}
