/* (C)2022 sjwi */
package com.sjwi.meals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MealPlannerWebApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(MealPlannerWebApplication.class, args);
  }
}
