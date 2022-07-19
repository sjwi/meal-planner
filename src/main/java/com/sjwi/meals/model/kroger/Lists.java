package com.sjwi.meals.model.kroger;

import java.util.UUID;

public class Lists {
  public List[] data;
  
  public static class List {
    public UUID id;
    public String versionKey;
    public String name;
    public Item[] items;
  }
}
