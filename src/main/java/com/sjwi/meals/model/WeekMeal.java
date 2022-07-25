/* (C)2022 https://stephenky.com */
package com.sjwi.meals.model;

import java.util.List;
import java.util.Map;

public class WeekMeal {

  private int id;
  private String name;
  private String recipeUrl;
  private String notes;
  private Map<Ingredient, Integer> ingredients;
  private Map<Integer, String> tags;
  private List<Side> sides;

  public WeekMeal(
      int id,
      String name,
      String recipeUrl,
      String notes,
      Map<Ingredient, Integer> ingredients,
      Map<Integer, String> tags,
      List<Side> sides) {
    this.id = id;
    this.name = name;
    this.recipeUrl = recipeUrl;
    this.notes = notes;
    this.ingredients = ingredients;
    this.tags = tags;
    this.sides = sides;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRecipeUrl() {
    return recipeUrl;
  }

  public String getNotes() {
    return notes;
  }

  public Map<Ingredient, Integer> getIngredients() {
    return ingredients;
  }

  public Map<Integer, String> getTags() {
    return tags;
  }

  public List<Side> getSides() {
    return sides;
  }
}
