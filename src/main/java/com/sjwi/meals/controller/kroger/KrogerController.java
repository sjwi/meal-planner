package com.sjwi.meals.controller.kroger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class KrogerController {

  @RequestMapping("/kroger/get-locations")
  @ResponseStatus(HttpStatus.OK)
  public void getLocations(){
    System.out.println("got 'eem");
  }
  
}
