package com.mealreminder.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.Date;

@Entity
public class MealReminder {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private Date reminderDatetime;
    private String food;
    private String status;

    public MealReminder(@NonNull Date reminderDatetime, String food, String status) {
        this.reminderDatetime = reminderDatetime;
        this.food = food;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date getReminderDatetime() {
        return reminderDatetime;
    }

    public void setReminderDatetime(Date reminderDatetime) {
        this.reminderDatetime = reminderDatetime;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
