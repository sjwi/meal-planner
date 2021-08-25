package com.sjwi.meals.model;

import java.util.List;

public class Side {
  private int id;
  private String name;
  private String notes;
  private String recipeUrl;
  private List<Ingredient> ingredients;

  public Side(int id, String name, String notes, String recipeUrl, List<Ingredient> ingredients) {
    this.id = id;
    this.name = name;
    this.notes = notes;
    this.recipeUrl = recipeUrl;
    this.ingredients = ingredients;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getNotes() {
    return notes;
  }

  public String getRecipeUrl() {
    return recipeUrl;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

}
