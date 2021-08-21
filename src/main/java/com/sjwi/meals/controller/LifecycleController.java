package com.sjwi.meals.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.Week;
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

    @RequestMapping(value = "/week/add-meal", method = RequestMethod.POST)
    @ResponseBody
    public Week addMealToWeek(@RequestParam Integer weekId, @RequestParam String meals, @RequestParam Integer addIndex) {
        if (weekId == 0) {
            List<Week> weeks = WeekGenerator.getWeeksForSelect(mealDao.getAllWeeks());
            weekId = mealDao.createWeek(weeks.get(addIndex - 1).getStart(),weeks.get(addIndex - 1).getEnd());
        }
        List<Integer> mealIds = new Gson().fromJson(meals, new TypeToken<ArrayList<Integer>>() {}.getType());
        mealDao.addMealsToWeek(weekId,mealIds);
        return mealDao.getWeekById(weekId);
    }
    
    @RequestMapping(value = "/week/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteWeek(@PathVariable int id) {
      mealDao.deleteWeek(id);
    }

    @RequestMapping(value = "meal/remove-from-week", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeMealFromWeek(@RequestParam int mealId, @RequestParam int weekId) {
      mealDao.removeMealFromWeek(mealId, weekId);
    }

    @RequestMapping(value = "meal/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createMeal(@RequestParam String inputMealName, @RequestParam(name="createEditFavorite", defaultValue = "false") Boolean favorite,
        @RequestParam(name="inputMealIngredients", required = false) List<Integer> ingredients, 
        @RequestParam(name="inputMealTags", required = false) List<Integer> tags, @RequestParam(name="inputMealNotes", required = false) String notes, 
        @RequestParam(name="inputRecipeUrl", required = false) String recipeUrl) {
      mealDao.createMeal(inputMealName, favorite, ingredients, tags, notes, recipeUrl);
      return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "meal/edit/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editMeal(@PathVariable int id, @RequestParam String inputMealName, 
        @RequestParam(name="createEditFavorite", defaultValue = "false") Boolean favorite,
        @RequestParam(name="inputMealIngredients", required = false) Set<Integer> ingredients, 
        @RequestParam(name="inputMealTags", required = false) Set<Integer> tags, @RequestParam(name="inputMealNotes", required = false) String notes, 
        @RequestParam(name="inputRecipeUrl", required = false) String recipeUrl) {
      Meal originalMeal = mealDao.getMealById(id);
      Set<Integer> originalIngredients = new HashSet<Ingredient>(originalMeal.getIngredients()).stream().map(i -> i.getId()).collect(Collectors.toSet());
      Set<Integer> originalTags = originalMeal.getTags().entrySet().stream().map(k -> k.getKey()).collect(Collectors.toSet());
      Set<Integer> ingredientsToDelete = SetUtils.difference(originalIngredients, ingredients);
      Set<Integer> tagsToDelete = SetUtils.difference(originalTags, tags);
      Set<Integer> ingredientsToAdd = SetUtils.difference(ingredients, originalIngredients);
      Set<Integer> tagsToAdd = SetUtils.difference(tags, originalTags);
      mealDao.editMeal(id,inputMealName,favorite,notes,recipeUrl,ingredientsToAdd,tagsToAdd,ingredientsToDelete,tagsToDelete);
      return new ModelAndView("redirect:/");
    }
}
