package com.calendar.cute.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.calendar.cute.utils.NotificationHelper;

// ==================== AlarmReceiver ====================
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int notificationId = intent.getIntExtra("notificationId", 0);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.showEventNotification(title, content, notificationId);
    }
}