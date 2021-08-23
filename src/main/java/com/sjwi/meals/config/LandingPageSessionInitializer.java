package com.sjwi.meals.config;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.MealsUser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LandingPageSessionInitializer {
  @Autowired
  MealDao mealDao;
  @Before("execution(* com.sjwi.meals.controller.HomeController.login(..))")
  public void attemptLoginViaCookie(JoinPoint joinPoint) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    if (SecurityContextHolder.getContext().getAuthentication() != null
						&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
				return;
    }
    if (request.getCookies() != null) {
      Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> com.sjwi.meals.service.AuthenticationService.STORED_COOKIE_TOKEN_KEY.equals(c.getName())).findFirst();
      if (cookie.isPresent()) {
        AbstractMap.SimpleEntry<String,String> token = mealDao.getStoredCookieToken(cookie.get().getValue());
        if (token.getValue() != null) {
          MealsUser user = (MealsUser) mealDao.getUser(token.getKey());
          SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        }
      }
    }

  }
  
}
