package com.sjwi.meals.model;

import java.util.List;

public class Side {
  private int id;
  private String name;
  private List<Ingredient> ingredients;

  public Side(int id, String name, List<Ingredient> ingredients) {
    this.id = id;
    this.name = name;
    this.ingredients = ingredients;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

}
