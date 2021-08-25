package com.sjwi.meals.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.Side;
import com.sjwi.meals.model.Week;
import com.sjwi.meals.service.MealService;
import com.sjwi.meals.util.WeekGenerator;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LifecycleController {

    @Autowired
    MealDao mealDao;

    @Autowired
    MealService mealService;

    @RequestMapping(value = "/week/add-meal", method = RequestMethod.POST)
    @ResponseBody
    public Week addMealToWeek(@RequestParam Integer weekId, @RequestParam String meals, 
      @RequestParam Integer addIndex, @RequestParam String mealSidesMap) {
        if (weekId == 0) {
            List<Week> weeks = WeekGenerator.getWeeksForSelect(mealDao.getAllWeeks());
            weekId = mealDao.createWeek(weeks.get(addIndex - 1).getStart(),weeks.get(addIndex - 1).getEnd());
        }
        List<Integer> mealIdsToAdd = new Gson().fromJson(meals, new TypeToken<ArrayList<Integer>>() {}.getType());
        List<Integer> existingMealIds = mealDao.getWeekById(weekId).getMeals().stream()
          .map(m -> m.getId())
          .collect(Collectors.toList());
        mealIdsToAdd.removeAll(existingMealIds);
        Map<Integer,List<Integer>> sidesMap = new Gson().fromJson(mealSidesMap, new TypeToken<HashMap<Integer,List<Integer>>>() {}.getType());
        mealDao.addMealsToWeek(weekId,mealIdsToAdd);
        mealDao.addSidesToMeals(mealIdsToAdd, sidesMap,weekId);
        return mealDao.getWeekById(weekId);
    }
    
    @RequestMapping(value = "/week/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteWeek(@PathVariable int id) {
      mealDao.deleteWeek(id);
    }
 
    @RequestMapping(value = "/meal/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteMeal(@PathVariable int id) {
      mealDao.deleteMeal(id);
    }
 
    @RequestMapping(value = "/side/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSide(@PathVariable int id) {
      mealDao.deleteSide(id);
    }
 
    @RequestMapping(value = "/meal/favorite/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deleteMeal(@RequestParam(name="toggle", required=false) boolean toggle, @PathVariable int id) {
      mealDao.toggleFavorite(id, toggle);
    }
 
    @RequestMapping(value = "/meal/remove-from-week", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeMealFromWeek(@RequestParam int mealId, @RequestParam int weekId) {
      mealDao.removeMealFromWeek(mealId, weekId);
    }

    @RequestMapping(value = "/meal/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createMeal(@RequestParam String inputMealName, @RequestParam(name="createEditFavorite", defaultValue = "false") Boolean favorite,
        @RequestParam(name="inputMealIngredients", defaultValue = "") Set<String> ingredients, 
        @RequestParam(name="inputMealTags", defaultValue = "") Set<String> tags, @RequestParam(name="inputMealNotes", required = false) String notes, 
        @RequestParam(name="inputRecipeUrl", required = false) String recipeUrl) {

      Set<Integer> createdIngredients = mealService.getItemIngredientsIdsToAdd(ingredients);
      Set<Integer> createdTags = mealService.getMealTagIdsToAdd(tags);

      mealDao.createMeal(inputMealName, favorite, createdIngredients, createdTags, notes, recipeUrl);
    }

    @RequestMapping(value = "/side/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createSide(@RequestParam String inputSideName,
        @RequestParam(name="inputSideIngredients", defaultValue = "") Set<String> ingredients) {

      Set<Integer> createdIngredients = mealService.getItemIngredientsIdsToAdd(ingredients);
      mealDao.createSide(inputSideName, createdIngredients);
    }

    @RequestMapping(value = "/side/edit/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void editSide(@PathVariable int id, @RequestParam String inputSideName,
        @RequestParam(name="inputSideIngredients", defaultValue = "") Set<String> ingredients) {

      Side originalSide = mealDao.getSideById(id);
      Set<Integer> existingIngredients = mealService.getItemIngredientsIdsToAdd(ingredients);
      Set<Integer> originalSideIngredients = new HashSet<Ingredient>(originalSide.getIngredients()).stream().map(i -> i.getId()).collect(Collectors.toSet());
      Set<Integer> ingredientsToDelete = SetUtils.difference(originalSideIngredients, existingIngredients);
      Set<Integer> ingredientsToAdd = SetUtils.difference(existingIngredients, originalSideIngredients);
      mealDao.editSide(id,inputSideName, ingredientsToAdd, ingredientsToDelete);
    }

    @RequestMapping(value = "/meal/edit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editMeal(@PathVariable int id, @RequestParam String inputMealName, 
        @RequestParam(name="createEditFavorite", defaultValue = "false") Boolean favorite,
        @RequestParam(name="inputMealIngredients", defaultValue =  "") Set<String> ingredients, 
        @RequestParam(name="inputMealTags", defaultValue = "") Set<String> tags, @RequestParam(name="inputMealNotes", required = false) String notes, 
        @RequestParam(name="inputRecipeUrl", required = false) String recipeUrl) {

      Meal originalMeal = mealDao.getMealById(id);

      Set<Integer> existingIngredients = mealService.getItemIngredientsIdsToAdd(ingredients);
      Set<Integer> existingTags = mealService.getMealTagIdsToAdd(tags);

      Set<Integer> originalIngredients = new HashSet<Ingredient>(originalMeal.getIngredients()).stream().map(i -> i.getId()).collect(Collectors.toSet());
      Set<Integer> originalTags = originalMeal.getTags().entrySet().stream().map(k -> k.getKey()).collect(Collectors.toSet());

      Set<Integer> ingredientsToDelete = SetUtils.difference(originalIngredients, existingIngredients);
      Set<Integer> tagsToDelete = SetUtils.difference(originalTags, existingTags);

      Set<Integer> ingredientsToAdd = SetUtils.difference(existingIngredients, originalIngredients);
      Set<Integer> tagsToAdd = SetUtils.difference(existingTags, originalTags);

      mealDao.editMeal(id,inputMealName,favorite,notes,recipeUrl,ingredientsToAdd,tagsToAdd,ingredientsToDelete,tagsToDelete);
      return new ModelAndView("redirect:/");
    }
}
