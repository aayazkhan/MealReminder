
package com.mealreminder.model;

import com.google.gson.annotations.SerializedName;

public class MealSchedule {

    @SerializedName("diet_duration")
    private int dietDuration;
    @SerializedName("week_diet_data")
    private WeekDietData weekDietData;

    public int getDietDuration() {
        return dietDuration;
    }

    public void setDietDuration(int dietDuration) {
        this.dietDuration = dietDuration;
    }

    public WeekDietData getWeekDietData() {
        return weekDietData;
    }

    public void setWeekDietData(WeekDietData weekDietData) {
        this.weekDietData = weekDietData;
    }

}
