package com.mealreminder.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date getDate(String value) {
        Type listType = new TypeToken<Date>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String getString(Date date) {
        Gson gson = new Gson();
        String json = gson.toJson(date);
        return json;
    }
}
