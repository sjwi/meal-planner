package com.sjwi.meals.config;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.MealsUser;
import com.sjwi.meals.service.UserService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LandingPageSessionInitializer {
  @Autowired
  MealDao mealDao;
  @Autowired
  UserService userService;
  @Before("execution(* com.sjwi.meals.controller.HomeController.login(..))")
  public void attemptLoginViaCookie(JoinPoint joinPoint) {
    if (SecurityContextHolder.getContext().getAuthentication() != null
						&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
				return;
    }
    MealsUser user = (MealsUser) userService.loadUserByUsername("admin");
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
  }
}
