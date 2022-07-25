/* (C)2022 https://stephenky.com */
package com.sjwi.meals.model.kroger;

public class Item {
  public Item(String upc, int quantity) {
    this.upc = upc;
    this.quantity = quantity;
  }

  public int quantity;
  public String upc;
}
