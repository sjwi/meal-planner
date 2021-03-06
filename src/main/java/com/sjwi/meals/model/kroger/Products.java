/* (C)2022 https://stephenky.com */
package com.sjwi.meals.model.kroger;

import java.util.Arrays;
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

    public String getImage(String size) {
      return Arrays.stream(images)
          .filter(i -> i.perspective.equals("front"))
          .map(i -> i.getSize(size))
          .findFirst()
          .orElse(images.length > 0 ? images[0].getSize(size) : "");
    }
  }

  public static class Image {
    public String perspective;
    public boolean featured;
    public Size[] sizes;

    public String getSize(String name) {
      return Arrays.stream(sizes)
          .filter(s -> s.size.equals(name))
          .map(s -> s.url)
          .findFirst()
          .orElse(sizes.length > 0 ? sizes[0].url : "");
    }
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
