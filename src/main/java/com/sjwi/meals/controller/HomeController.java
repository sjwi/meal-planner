package com.sjwi.meals.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Week;
import com.sjwi.meals.util.WeekGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    MealDao mealDao;

    @RequestMapping("/")
    public ModelAndView homeController(){
        ModelAndView mv = new ModelAndView("home");
        List<Week> weeks = mealDao.getAllWeeks();
        mv.addObject("meals", mealDao.getAllMeals());
        mv.addObject("weeks", weeks);
        mv.addObject("weeksForSelect", WeekGenerator.getWeeksForSelect(weeks));
        return mv;
    }

    @RequestMapping("/meals")
    public ModelAndView meals(){
        ModelAndView mv = new ModelAndView("home :: mealList");
        mv.addObject("meals", mealDao.getAllMeals());
        return mv;
    }

    @RequestMapping("/weeks")
    public ModelAndView weeks(){
        ModelAndView mv = new ModelAndView("home :: weekList");
        mv.addObject("weeks", mealDao.getAllWeeks());
        return mv;
    }

    @RequestMapping("/week/ingredients/{id}")
    public ModelAndView getIngredientsInWeek(@PathVariable int id) {
        Week week = mealDao.getWeekById(id);
        List<String> ingredients = week.getMeals().stream()
            .map(m -> m.getIngredients())
            .flatMap(List::stream)
            .map(i -> i.getName())
            .collect(Collectors.toList());
        Map<String,Integer> ingredientMap = new HashMap<>();
        for (String ingredient : ingredients){
            if (ingredientMap.containsKey(ingredient))
                ingredientMap.put(ingredient, ingredientMap.get(ingredient) + 1);
            else
                ingredientMap.put(ingredient,1);
        }
        ModelAndView mv = new ModelAndView("modal/dynamic/ingredients");
        mv.addObject("ingredients", ingredientMap);
        return mv;
    }
    @RequestMapping("/week/details/{id}")
    public ModelAndView getWeekDetails(@PathVariable int id, @RequestParam String view) {
        Week week = mealDao.getWeekById(id);
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("week", week);
        return mv;
    }

    @RequestMapping("/meal/details/{id}")
    public ModelAndView getMealDetails(@PathVariable int id, @RequestParam String view) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("meal", mealDao.getMealById(id));
        return mv;
    }
}
