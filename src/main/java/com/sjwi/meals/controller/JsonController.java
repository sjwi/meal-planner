package com.sjwi.meals.controller;

import java.util.List;
import java.util.Map;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonController {
  @Autowired
  MealDao mealDao;

  @RequestMapping("/json/tags")
  @ResponseBody
  public Map<Integer,String> getAllTags() {
    return mealDao.getAllTags();
  }
  
  @RequestMapping("/json/meal/{id}")
  @ResponseBody
  public Meal getMealById(@PathVariable int id) {
    return mealDao.getMealById(id);
  }
  
  @RequestMapping("/json/ingredients")
  @ResponseBody
  public List<Ingredient> getAllIngredients() {
    return mealDao.getAllIngredients();
  }

}
