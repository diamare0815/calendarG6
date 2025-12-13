package com.calendar.cute.Notification;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.app.PendingIntent;
import android.os.Build;

import com.calendar.cute.MainActivity;
import com.calendar.cute.R;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TIME = "extra_time";

    private static final String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String time = intent.getStringExtra(EXTRA_TIME);
            Log.d(TAG, "onReceive: title=" + title + " time=" + time);

            // Intent để mở app khi người dùng nhấn notification
            Intent openApp = new Intent(context, MainActivity.class);
            openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openApp,
                    PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "event_channel")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Event Reminder")
                    .setContentText((title != null ? title : "Event") + (time != null ? " at " + time : ""))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);

            // Kiểm tra permission POST_NOTIFICATIONS trên Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "Missing POST_NOTIFICATIONS permission - skipping notify()");
                    // Không thể yêu cầu quyền trong BroadcastReceiver; bạn có thể notify user bằng cách mở app
                    return;
                }
            }

            try {
                manager.notify((int) System.currentTimeMillis(), builder.build());
            } catch (SecurityException se) {
                Log.e(TAG, "SecurityException when notify: " + se.getMessage(), se);
            }

            // Optional: If you want to open Clock app (not recommended automatically):
            // Intent alarmIntent = new Intent(android.provider.AlarmClock.ACTION_SHOW_ALARMS);
            // alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // if (alarmIntent.resolveActivity(context.getPackageManager()) != null) {
            //     context.startActivity(alarmIntent);
            // }

            // Optional: send SMS (requires SEND_SMS permission and runtime request in an Activity)
            // NOTE: sending SMS may cost the user money.
            // String phoneNumber = "0123456789";
            // String smsText = "Reminder: " + title + " at " + time;
            // SmsManager smsManager = SmsManager.getDefault();
            // smsManager.sendTextMessage(phoneNumber, null, smsText, null, null);

        } catch (Exception e) {
            Log.e("ReminderReceiver", "Exception in onReceive", e);
        }
    }
}
