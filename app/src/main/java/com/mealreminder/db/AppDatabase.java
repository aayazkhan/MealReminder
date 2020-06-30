package com.mealreminder.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mealreminder.db.converters.DateConverter;
import com.mealreminder.db.dao.MealReminderDao;
import com.mealreminder.model.MealReminder;

@Database(
        entities = {
                MealReminder.class,
        },
        version = 1
)
@TypeConverters({
        DateConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase = null;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "meal.db").allowMainThreadQueries().build();
        }
        return appDatabase;
    }


    public abstract MealReminderDao getMealReminderDao();

}
