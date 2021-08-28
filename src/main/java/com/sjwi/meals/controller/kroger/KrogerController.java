package com.sjwi.meals.controller.kroger;

import java.net.URISyntaxException;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Products;
import com.sjwi.meals.service.KrogerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class KrogerController {

  @Autowired
  MealDao mealDao;

  @Autowired
  KrogerService krogerService;

  @RequestMapping("/kroger/searchProducts")
  @ResponseStatus(HttpStatus.OK)
  public ModelAndView getLocations(@RequestParam int ingredientId) throws URISyntaxException{
    Ingredient ingredient = mealDao.getIngredientById(ingredientId);
    System.out.println("got 'eem");
    Products products = krogerService.getProductsByTerm(ingredient.getName());
    ModelAndView mv = new ModelAndView("modal/dynamic/kroger");
    mv.addObject("products", products);
    return mv;
  }
  
}
