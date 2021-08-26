package com.sjwi.meals.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.MealsUser;
import com.sjwi.meals.model.Week;
import com.sjwi.meals.service.AuthenticationService;
import com.sjwi.meals.service.MealService;
import com.sjwi.meals.util.WeekGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @Autowired
  MealDao mealDao;

  @Autowired
  MealService mealService;

  @Autowired
  AuthenticationService authenticationService;

  private static final int DEFAULT_NUMBER_OF_WEEKS = 25;

  @RequestMapping("/")
  public ModelAndView homeController(Authentication auth) {
    Map<String, String> preferences  = ((MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPreferences();
    // Map<String, String> preferences = ((MealsUser) auth.getPrincipal()).getPreferences();
    ModelAndView mv = new ModelAndView("home");
    List<Week> weeks = mealDao.getNNumberOfWeeks(DEFAULT_NUMBER_OF_WEEKS);
    mv.addObject("meals", mealDao.getAllMeals(preferences));
    mv.addObject("sides", mealDao.getAllSides());
    mv.addObject("weeks", weeks);
    mv.addObject("weeksForSelect", WeekGenerator.getWeeksForSelect(weeks));
    return mv;
  }

  @RequestMapping(value = "/update-preferences", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void setPreferences(@RequestParam(name="pinFavorites", required = false) boolean pinFavorites,
      @RequestParam String sortBy, @RequestParam String sortOrder, Authentication auth) {
    mealService.setPreferences(pinFavorites,sortBy,sortOrder, ((MealsUser) auth.getPrincipal()).getUsername());
  }

  @RequestMapping("/search/meals")
  public ModelAndView search(@RequestParam(required = false) String searchTerm, Authentication auth,
      @RequestParam(value="tags[]",required = false) List<Integer> tags) {
    Map<String, String> preferences = ((MealsUser) auth.getPrincipal()).getPreferences();
    if (tags == null)
        tags = new ArrayList<Integer>();
    ModelAndView mv = new ModelAndView("home :: mealList");
    mv.addObject("meals", mealDao.searchMeals(searchTerm, tags, preferences));
    return mv;
  }


  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView login() {
    if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails)
      return new ModelAndView("redirect:/");
    return new ModelAndView("login");
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ModelAndView postLogin(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "username", required = true) String username, Principal principal, Authentication auth,
      @RequestParam(name = "password", required = true) String password) throws ServletException {
    request.login(username, password);
    authenticationService.generateCookieToken(request, response, username);
    return new ModelAndView("redirect:/");
  }

  @RequestMapping("/meals")
  public ModelAndView meals(Authentication auth) {
    Map<String, String> preferences = ((MealsUser) auth.getPrincipal()).getPreferences();
    ModelAndView mv = new ModelAndView("home :: mealList");
    mv.addObject("meals", mealDao.getAllMeals(preferences));
    return mv;
  }

  @RequestMapping("/weeks")
  public ModelAndView weeks(@RequestParam boolean showAll) {
    ModelAndView mv = new ModelAndView("home :: weekList");
    if (showAll)
      mv.addObject("weeks", mealDao.getAllWeeks());
    else 
      mv.addObject("weeks", mealDao.getNNumberOfWeeks(DEFAULT_NUMBER_OF_WEEKS));
    return mv;
  }

  @RequestMapping("/sides")
  public ModelAndView sides(@RequestParam(required=false) String searchTerm) {
    ModelAndView mv = new ModelAndView("home :: sideList");
    if (searchTerm != null && !searchTerm.trim().isEmpty())
      mv.addObject("sides", mealDao.searchSides(searchTerm));
    else
      mv.addObject("sides", mealDao.getAllSides());
    return mv;
  }


  @RequestMapping("/week/ingredients/{id}")
  public ModelAndView getIngredientsInWeek(@PathVariable int id) {
    Week week = mealDao.getWeekById(id);

    Map<Ingredient, Integer> ingredientMaster = new HashMap<>();
    week.getMeals().stream().map(m -> m.getIngredients())
        .forEach(im -> {
          for (Map.Entry<Ingredient,Integer> entry : im.entrySet()){
            if (ingredientMaster.containsKey(entry.getKey()))
              ingredientMaster.put(entry.getKey(), ingredientMaster.get(entry.getKey()) + entry.getValue());
            else
              ingredientMaster.put(entry.getKey(), entry.getValue());
          }
        });
    ModelAndView mv = new ModelAndView("modal/dynamic/ingredients");
    mv.addObject("ingredients", ingredientMaster);
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

  @RequestMapping("/week-meal/details/{id}")
  public ModelAndView getWeekMealDetails(@PathVariable int id, @RequestParam String view, @RequestParam int weekId) {
    ModelAndView mv = new ModelAndView(view);
    mv.addObject("meal", mealDao.getWeekMealById(id,weekId));
    return mv;
  }
}
