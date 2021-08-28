package com.sjwi.meals.model;

import java.util.Map;

public class Products {
  public Product[] data;

  public static class Product {
    public String productId;
    public String upc;
    public Map<String, String>[] aisleLocations;
    public String brand;
    public String[] categories;
    public String countryOrigin;
    public String description;
    public Image[] images;
    public Item[] items;
    public Map<String, String> itemInformation;
    public Map<String, Object> temperature;
  }

  public static class Image {
    public String perspective;
    public boolean featured;
    public Size[] sizes;
  }

  public static class Size {
    public String size;
    public String url;
  }

  public static class Item {
    public String itemId;
    public boolean favorite;
    public Map<String, Boolean> fulfillment;
    public Map<String, Double> price;
    public String size;
    public String soldBy;
  }
}
