package com.mealreminder.db;

import android.content.Context;

import com.mealreminder.db.dao.MealReminderDao;

public class DatabaseRepository {

    private AppDatabase appDatabase;

    private MealReminderDao mealReminderDao;

    public DatabaseRepository(Context context) {

        appDatabase = AppDatabase.getInstance(context);

        mealReminderDao = appDatabase.getMealReminderDao();

    }

    public void clearAllTables() {
        appDatabase.clearAllTables();
    }

    public MealReminderDao getMealReminderDao() {
        return mealReminderDao;
    }

}
