package com.sjwi.meals.controller.kroger;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.kroger.Locations;
import com.sjwi.meals.model.kroger.Products;
import com.sjwi.meals.model.security.MealsUser;
import com.sjwi.meals.service.KrogerService;

@Controller
public class KrogerController {

  private static final String KROGER_LOCATION_KEY = "krogerLocationId";

  @Autowired
  MealDao mealDao;

  @Autowired
  KrogerService krogerService;

  @RequestMapping("/kroger/searchProducts")
  @ResponseStatus(HttpStatus.OK)
  public ModelAndView searchProducts(@RequestParam String term, @RequestParam(defaultValue = "1") Integer pageCount) throws URISyntaxException, UnsupportedEncodingException{
    Map<String, String> preferences  = ((MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPreferences();
    Products products = krogerService.getProductsByTerm(term, preferences.get(KROGER_LOCATION_KEY), pageCount);
    ModelAndView mv = new ModelAndView("modal/dynamic/kroger");
    mv.addObject("products", products);
    mv.addObject("searchedTerm", term);
    mv.addObject("pageCount", pageCount);
    return mv;
  }
  
  @RequestMapping(value = "/kroger/addProductToCart", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void addProductToCart(@RequestParam String upc, @RequestParam int count) throws URISyntaxException, UnsupportedEncodingException{
    krogerService.addProductToCart(upc, count);
  }

  @RequestMapping(value = "/kroger/addProductToList", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void addProductToList(@RequestParam String upc,
      @RequestParam int count,
      @RequestParam String weekName) throws URISyntaxException, UnsupportedEncodingException{
    krogerService.addProductToList(weekName, upc, count);
  }
  @RequestMapping("/user-preferences")
  public ModelAndView getUserPreferences(){
    ModelAndView mv = new ModelAndView("modal/dynamic/preferences");
    mv.addObject("locations", krogerService.getLocationsByZip("40517"));
    return mv;
  }

  @RequestMapping("/json/kroger-locations")
  @ResponseBody
  public Locations getAllSides(@RequestParam String zipCode) {
    return krogerService.getLocationsByZip(zipCode);
  }
}
