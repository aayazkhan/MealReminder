package com.mealreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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

        showNotification(getApplicationContext(), mealReminder, new Intent(getApplicationContext(), MainActivity.class));

        mealReminder.setStatus("COMPLETED");
        databaseRepository.getMealReminderDao().update(mealReminder);

        mealReminder = databaseRepository.getMealReminderDao().getMealReminder();
        setAlarm(mealReminder);

    }

    public void showNotification(Context context, MealReminder mealReminder, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "Meal_Reminder";
        String channelName = "Meal Reminder";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Meal Reminder")
                .setContentText(mealReminder.getFood());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
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
