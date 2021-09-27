package com.sjwi.meals.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sjwi.meals.dao.MealDao;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MealService {

  @Autowired
  MealDao mealDao;

  @Autowired
  UserService userService;

  public Set<Integer> getItemIngredientsIdsToAdd(Set<String> rawIngredients) {
    List<String> ingredientsToCreate = new ArrayList<>();
    Set<Integer> existingIngredients = new HashSet<>();
    rawIngredients.forEach(i -> {
      if (!NumberUtils.isCreatable(i))
        ingredientsToCreate.add(i.toLowerCase());
      else
        existingIngredients.add(Integer.parseInt(i));
    });
    Set<Integer> createdIngredientIds = mealDao.createIngredients(ingredientsToCreate);
    existingIngredients.addAll(createdIngredientIds);
    return existingIngredients;
  }

  public Set<Integer> getMealTagIdsToAdd(Set<String> rawTags) {
    List<String> tagsToCreate = new ArrayList<>();
      Set<Integer> existingTags = new HashSet<>();
      rawTags.forEach(t -> {
        if (!NumberUtils.isCreatable(t))
          tagsToCreate.add(t.toLowerCase());
        else
          existingTags.add(Integer.parseInt(t));
      });

      Set<Integer> createdTagIds = mealDao.createTags(tagsToCreate);
      existingTags.addAll(createdTagIds);
      return existingTags;
  }

  public void setPreferences(boolean pinFavorites, String sortBy, String sortOrder, int weekStartDay, String locationId, String username) {
    mealDao.updatePreferences(pinFavorites, sortBy, sortOrder, weekStartDay, locationId, username);
    userService.refreshUserState(username);
  }
}
