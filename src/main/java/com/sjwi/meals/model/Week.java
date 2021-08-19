package com.sjwi.meals.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Week {
    private int id;
    private Date start;
    private Date end;
    private List<Meal> meals;

    public Week(int id, Date start, Date end, List<Meal> meals) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.meals = meals;
    }

    public Week(Date start, Date end) {
        this.id = 0;
        this.start = start;
        this.end = end;
        this.meals = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public Date getStart() {
        return start;
    }
    public Date getEnd() {
        return end;
    }
    public List<Meal> getMeals() {
        return meals;
    }
}
