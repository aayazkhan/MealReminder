package com.mealreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.mealreminder.db.DatabaseRepository;
import com.mealreminder.model.MealReminder;

import java.util.Calendar;

public class MealReminderService extends JobIntentService {
    private static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MealReminderService.class, JOB_ID, intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleWork(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        DatabaseRepository databaseRepository = new DatabaseRepository(getApplicationContext());
        MealReminder mealReminder = databaseRepository.getMealReminderDao().getMealReminder();

        addNotification(mealReminder);

        mealReminder.setStatus("COMPLETED");
        databaseRepository.getMealReminderDao().update(mealReminder);

        mealReminder = databaseRepository.getMealReminderDao().getMealReminder();
        setAlarm(mealReminder);

    }

    private void addNotification(MealReminder mealReminder) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "MEAL_REMINDER");
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("MealReminder")
                .setContentTitle("Meal Reminder")
                .setContentText(mealReminder.getFood())
                .setContentInfo(mealReminder.getFood());

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private void setAlarm(MealReminder mealReminder) {

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mealReminder.getReminderDatetime());
        calendar.add(Calendar.MINUTE, -5);

        long alarmTime = calendar.getTimeInMillis();
        System.out.println("alarmTime:" + alarmTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTime, pendingIntent), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, alarmTime, 1, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }

    }

}
