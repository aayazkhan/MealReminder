
package com.mealreminder.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class WeekDietData {

    @Expose
    private List<Monday> monday;
    @Expose
    private List<Thursday> thursday;
    @Expose
    private List<Wednesday> wednesday;

    public List<Monday> getMonday() {
        return monday;
    }

    public void setMonday(List<Monday> monday) {
        this.monday = monday;
    }

    public List<Thursday> getThursday() {
        return thursday;
    }

    public void setThursday(List<Thursday> thursday) {
        this.thursday = thursday;
    }

    public List<Wednesday> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<Wednesday> wednesday) {
        this.wednesday = wednesday;
    }

}
