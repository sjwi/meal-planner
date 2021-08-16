package com.sjwi.meals.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    MealDao mealDao;

    @RequestMapping("/")
    public ModelAndView homeController(){
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("meals", mealDao.getAllMeals());
        mv.addObject("weeks", mealDao.getAllWeeks());
        mv.addObject("tags", mealDao.getAllTags());
        return mv;
    }

    @RequestMapping("/meal/edit/{id}")
    public ModelAndView editMeal(@PathVariable int id) {
        ModelAndView mv = new ModelAndView("modal/dynamic/create-edit");
        Meal meal = mealDao.getMealById(id);
        List<Integer> ingredientIds = meal.getIngredients() == null?
            new ArrayList<Integer>(): 
            meal.getIngredients().stream().map(i -> i.getId())
                .collect(Collectors.toList());
        mv.addObject("meal", meal);
        mv.addObject("tags", mealDao.getAllTags());
        mv.addObject("ingredients", mealDao.getAllIngredients());
        mv.addObject("ingredientIds", ingredientIds);
        return mv;
    }
    
    @RequestMapping("/meal/create")
    public ModelAndView createMeal() {
        ModelAndView mv = new ModelAndView("modal/dynamic/create-edit");
        mv.addObject("tags", mealDao.getAllTags());
        mv.addObject("ingredients", mealDao.getAllIngredients());
        return mv;
    }
}
