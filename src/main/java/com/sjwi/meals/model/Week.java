package com.sjwi.meals.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Week {
    private int id;
    private Date start;
    private Date end;
    private List<WeekMeal> meals;

    public Week(int id, Date start, Date end, List<WeekMeal> meals) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.meals = meals;
    }

    public boolean isNext() {
        LocalDate now = LocalDate.now();
        LocalDate nextWeek = now.plus(1, ChronoUnit.WEEKS);
        Date oneWeekLaterDate = Date.from(nextWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return end.compareTo(oneWeekLaterDate) > 0 && oneWeekLaterDate.compareTo(start) > 0;
    }

    public boolean isCurrent() {
        Date now = new Date();
        return end.compareTo(now) > 0 && now.compareTo(start) > 0;
    }

    public Week(Date start, Date end) {
        this.id = 0;
        this.start = start;
        this.end = end;
        this.meals = new ArrayList<>();
    }

    public String getKey() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(start) + simpleDateFormat.format(end);
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
    public List<WeekMeal> getMeals() {
        return meals;
    }
}
