package com.mealreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //start service
        PowerManager.WakeLock screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, MealReminderService.class.getName());
        screenLock.setReferenceCounted(true);
        screenLock.acquire();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            intent = new Intent(context, MealReminderService.class);
            MealReminderService.enqueueWork(context, intent);
        } else {
            context.startService(new Intent(context, MealReminderService.class));
        }
        screenLock.release();
    }

}