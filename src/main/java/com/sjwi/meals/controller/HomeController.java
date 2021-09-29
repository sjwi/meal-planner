package com.sjwi.meals.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.meals.config.LandingPageSessionInitializer;
import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.log.CustomLogger;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Week;
import com.sjwi.meals.model.security.AccessTokenResponse;
import com.sjwi.meals.model.security.MealsUser;
import com.sjwi.meals.service.KrogerService;
import com.sjwi.meals.service.MealService;
import com.sjwi.meals.service.UserService;
import com.sjwi.meals.util.WeekGenerator;
import com.sjwi.meals.util.security.AuthenticationService;
import com.sjwi.meals.util.security.JwtManager;
import com.sjwi.meals.util.security.OAuthManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

  @Autowired
  MealDao mealDao;

  @Autowired
  MealService mealService;

  @Autowired
  KrogerService krogerService;

  @Autowired
  OAuthManager oAuthManager;

  @Autowired
  UserService userService;

  @Autowired
  AuthenticationService authenticationService;

  private static final int DEFAULT_NUMBER_OF_WEEKS = 25;

  @RequestMapping("/")
  public ModelAndView homeController(HttpServletRequest request, Authentication auth) {
    Map<String, String> preferences  = ((MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPreferences();
    ModelAndView mv = new ModelAndView("home");
    List<Week> weeks = mealDao.getNNumberOfWeeks(DEFAULT_NUMBER_OF_WEEKS);
    WeekGenerator weekGenerator = new WeekGenerator(Integer.parseInt(preferences.get("weekStartDay")));
    mv.addObject("meals", mealDao.getAllMeals(preferences));
    mv.addObject("sides", mealDao.getAllSides());
    mv.addObject("weeks", weeks);
    mv.addObject("weeksForSelect", weekGenerator.getWeeksForSelect(weeks));
    return mv;
  }

  @RequestMapping(value = "/update-preferences", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void setPreferences(@RequestParam(name="prefPreferFavorites", required = false) boolean pinFavorites,
      @RequestParam String prefSortBy, @RequestParam String prefSortOrder, @RequestParam int prefWeekStartDay,
      @RequestParam String prefKrogerLocationId, Authentication auth) {
    mealService.setPreferences(pinFavorites,prefSortBy,prefSortOrder, prefWeekStartDay,
      prefKrogerLocationId, ((MealsUser) auth.getPrincipal()).getUsername());
  }

  @RequestMapping(value = "/update-account", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void updateAccount(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, Principal principal) {
    userService.updateAccount(principal.getName(),firstName,lastName,email);
  }

  @RequestMapping(value = "/user-account/delete", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteAccount(Principal principal) {
    userService.deleteAccount(principal.getName());
  }

  @RequestMapping("/search/meals")
  public ModelAndView search(@RequestParam(required = false) String searchTerm, Authentication auth,
      @RequestParam(value="tags[]",required = false) List<Integer> tags,
      @RequestParam(name="pinFavorites", required = false) boolean pinFavorites,
      @RequestParam String sortBy, @RequestParam String sortOrder) {
    Map<String, String> searchParams = new HashMap<>();
    searchParams.put("sort",sortBy);
    searchParams.put("sortDirection",sortOrder);
    searchParams.put("pinFavorites",pinFavorites? "1":"0");
    if (tags == null)
        tags = new ArrayList<Integer>();
    ModelAndView mv = new ModelAndView("home :: mealList");
    mv.addObject("meals", mealDao.searchMeals(searchTerm, tags, searchParams));
    return mv;
  }

  @Autowired
  CustomLogger logger;

  @Autowired
  LandingPageSessionInitializer sessionInitializer;

  @RequestMapping(value="/refresh-login")
  @ResponseStatus(HttpStatus.OK)
  public void refreshLogin() {
    logger.debug("In refresh");
    if (SecurityContextHolder.getContext().getAuthentication() != null
						&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
				return;
    }
    logger.debug("Refreshing state");
    sessionInitializer.refreshUserSession();
  }


  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView login() {
    if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails)
      return new ModelAndView("redirect:/");
    return new ModelAndView("redirect:" + oAuthManager.getSignOnUrl());
  }

  @RequestMapping(value = "/oauth2/login", method = RequestMethod.GET)
  public ModelAndView krogerLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
    ModelAndView mv = new ModelAndView("redirect:/");
    AccessTokenResponse tokenResponse = oAuthManager.getOAuthToken(code);
    JwtManager jwtManager = new JwtManager(tokenResponse);
    request.getSession().setAttribute("JWT", tokenResponse.getAccess_token());
    request.getSession().setAttribute("JWT_EXPIRES_ON", oAuthManager.getExpirationDate(tokenResponse.getExpires_in()));

    User user = mealDao.getUser(jwtManager.getOAuthUser());
    if (user == null) {
      user = mealDao.registerNewUser(jwtManager.getOAuthUser(), tokenResponse.getRefresh_token());
      authenticationService.generateCookieToken(jwtManager.getOAuthUser());
      redirectAttributes.addFlashAttribute("REGISTER_USER",true);
    }
    else {
      user = mealDao.updateUserRefreshToken(jwtManager.getOAuthUser(),tokenResponse.getRefresh_token());
      if (!authenticationService.userHasLoginCookie())
        authenticationService.generateCookieToken(jwtManager.getOAuthUser());
    }
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    return mv;
  }

  @RequestMapping("/meals")
  public ModelAndView meals(Authentication auth) {
    Map<String, String> preferences = ((MealsUser) auth.getPrincipal()).getPreferences();
    ModelAndView mv = new ModelAndView("home :: mealList");
    mv.addObject("meals", mealDao.getAllMeals(preferences));
    return mv;
  }

  @RequestMapping("/recipe/hosted/{mealId}")
  public ModelAndView hostedRecipe(@PathVariable int mealId) {
    ModelAndView mv = new ModelAndView("recipe-images");
    mv.addObject("recipeUrls", mealDao.getRecipeImagesForMeal(mealId));
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

  @RequestMapping("/search-modal")
  public ModelAndView searchModal() {
    return new ModelAndView("modal/search :: searchForm");
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
