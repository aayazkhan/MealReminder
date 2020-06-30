package com.mealreminder.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mealreminder.model.MealReminder;

import java.util.List;

@Dao
public interface MealReminderDao {

    @Insert
    void insert(List<MealReminder> mealReminder);

    @Update
    void update(MealReminder mealReminder);

    @Query("DELETE FROM MealReminder")
    void delete();

    @Query("SELECT * FROM MealReminder where status = 'PENDING' order by _id ASC LIMIT 1")
    MealReminder getMealReminder();

}
