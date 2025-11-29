package com.calendar.cute.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.calendar.cute.utils.NotificationHelper;

// ==================== AlarmReceiver ====================
class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Reschedule all alarms here
            // This would require loading all events/todos from database
            // and rescheduling their reminders
        }
    }
}