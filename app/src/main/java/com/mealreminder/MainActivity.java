package com.mealreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mealreminder.databinding.ActivityMainBinding;
import com.mealreminder.db.DatabaseRepository;
import com.mealreminder.model.MealReminder;
import com.mealreminder.model.MealSchedule;
import com.mealreminder.model.Monday;
import com.mealreminder.model.Thursday;
import com.mealreminder.model.Wednesday;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private DatabaseRepository databaseRepository;

    private final String data = "{\"diet_duration\": 20, \"week_diet_data\": {\"monday\": [{\"food\": \"Warm honey and water\", \"meal_time\": \"07:00\"}, {\"food\": \"proper thali\", \"meal_time\": \"15:00\"}], \"wednesday\": [{\"food\": \"Sprouts\", \"meal_time\": \"07:00\"}, {\"food\": \"Bread lintils and Rice\", \"meal_time\": \"16:00\"}, {\"food\": \"Soup ,Rice and Chicken\", \"meal_time\": \"21:00\"}], \"thursday\": [{\"food\": \"scramblled eggs\", \"meal_time\": \"08:00\"}, {\"food\": \"Burrito bowls\", \"meal_time\": \"14:00\"}, {\"food\": \"Evening snacks\", \"meal_time\": \"18:00\"}, {\"food\": \"North Indian thali\", \"meal_time\": \"22:00\"}]}}";

    private MealSchedule mealSchedule;

    private List<MealReminder> mealReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        databaseRepository = new DatabaseRepository(getApplicationContext());

        mealSchedule = new Gson().fromJson(data, MealSchedule.class);

        mealReminders = new LinkedList<>();

        binding.buttonStart.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            calendar.add(Calendar.DATE, mealSchedule.getDietDuration());
            Date maxDate = calendar.getTime();

            calendar.add(Calendar.DATE, -mealSchedule.getDietDuration());
            Date date = calendar.getTime();

            while (date.getTime() <= maxDate.getTime()) {

                int i = calendar.get(Calendar.DAY_OF_WEEK);

                if (i == 1) {

                } else if (i == 2) { // monday

                    for (Monday monday : mealSchedule.getWeekDietData().getMonday()) {
                        String[] time = monday.getMealTime().split(":");
                        Date scheduleDate = generateDateTime(calendar, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);

                        mealReminders.add(new MealReminder(scheduleDate, monday.getFood(), "PENDING"));
                    }

                } else if (i == 3) {

                } else if (i == 4) { // wednesday

                    for (Wednesday wednesday : mealSchedule.getWeekDietData().getWednesday()) {
                        String[] time = wednesday.getMealTime().split(":");
                        Date scheduleDate = generateDateTime(calendar, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);

                        mealReminders.add(new MealReminder(scheduleDate, wednesday.getFood(), "PENDING"));
                    }

                } else if (i == 5) { // thursday

                    for (Thursday thursday : mealSchedule.getWeekDietData().getThursday()) {
                        String[] time = thursday.getMealTime().split(":");
                        Date scheduleDate = generateDateTime(calendar, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);

                        mealReminders.add(new MealReminder(scheduleDate, thursday.getFood(), "PENDING"));
                    }

                } else if (i == 6) {

                } else if (i == 7) {

                }
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
            }

            databaseRepository.getMealReminderDao().insert(mealReminders);

            setAlarm(databaseRepository.getMealReminderDao().getMealReminder());

        });


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

    private Date generateDateTime(@NonNull Calendar calendar, int hour, int min, int sec) {

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        Date scheduleDateTime = calendar.getTime();

        Log.d(TAG, "generateDateTime: scheduleDateTime=>" + scheduleDateTime);
        Log.d(TAG, "<<============================================>>");

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return scheduleDateTime;
    }

}