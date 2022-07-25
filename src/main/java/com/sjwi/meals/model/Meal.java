/* (C)2022 sjwi */
package com.sjwi.meals.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Meal {

  private int id;
  private String name;
  private int count;
  private Date lastEaten;
  private boolean favorite;
  private String recipeUrl;
  private String notes;
  private List<Ingredient> ingredients;
  private Map<Integer, String> tags;

  public Meal(
      int id,
      String name,
      int count,
      Date lastEaten,
      boolean favorite,
      String recipeUrl,
      String notes,
      List<Ingredient> ingredients,
      Map<Integer, String> tags) {
    this.id = id;
    this.name = name;
    this.count = count;
    this.lastEaten = lastEaten;
    this.favorite = favorite;
    this.recipeUrl = recipeUrl;
    this.notes = notes;
    this.ingredients = ingredients;
    this.tags = tags;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCount() {
    return count;
  }

  public Date getLastEaten() {
    return lastEaten;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public String getRecipeUrl() {
    return recipeUrl;
  }

  public String getNotes() {
    return notes;
  }

  @JsonIgnore
  public long getWeeksSinceLastEaten() {
    Calendar calLastEaten = Calendar.getInstance();
    calLastEaten.setTime(lastEaten);
    Instant d1i = Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis());
    Instant d2i = Instant.ofEpochMilli(calLastEaten.getTimeInMillis());

    LocalDateTime startDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
    LocalDateTime endDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());

    return ChronoUnit.WEEKS.between(startDate, endDate);
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public Map<Integer, String> getTags() {
    return tags;
  }
}
