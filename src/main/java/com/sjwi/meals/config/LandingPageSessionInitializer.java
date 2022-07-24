/* (C)2022 sjwi */
package com.sjwi.meals.config;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.security.AccessTokenResponse;
import com.sjwi.meals.model.security.MealsUser;
import com.sjwi.meals.util.security.AuthenticationService;
import com.sjwi.meals.util.security.OAuthManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LandingPageSessionInitializer {
  @Autowired MealDao mealDao;

  @Autowired OAuthManager oAuthManager;

  @Autowired AuthenticationService authenticationService;

  @Value("${meals.settings.federateIdentity}")
  Boolean federateIdentity;

  @Before("execution(* com.sjwi.meals.controller.HomeController.login(..))")
  public void attemptLoginViaCookie(JoinPoint joinPoint) {
    if (!federateIdentity) {
      MealsUser localUser = (MealsUser) mealDao.getUser("de9a8c23-bb74-512d-a390-2b8cb659ebcf");
      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(localUser, null, localUser.getAuthorities()));
      return;
    }
    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            instanceof UserDetails) {
      return;
    }
    refreshUserSession();
  }

  public void refreshUserSession() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    if (request.getCookies() != null) {
      Optional<Cookie> cookie =
          Arrays.stream(request.getCookies())
              .filter(
                  c ->
                      com.sjwi.meals.util.security.AuthenticationService.STORED_COOKIE_TOKEN_KEY
                          .equals(c.getName()))
              .findFirst();
      if (cookie.isPresent()) {
        AbstractMap.SimpleEntry<String, String> token =
            mealDao.getStoredCookieToken(cookie.get().getValue());
        if (token.getValue() != null) {
          MealsUser user = (MealsUser) mealDao.getUser(token.getKey());
          if (user != null
              && user.getRefreshToken() != null
              && !user.getRefreshToken().trim().isEmpty()) {
            try {
              AccessTokenResponse tokenResponse =
                  oAuthManager.refreshAccessToken(user.getRefreshToken());
              request.getSession().setAttribute("JWT", tokenResponse.getAccess_token());
              request
                  .getSession()
                  .setAttribute(
                      "JWT_EXPIRES_ON",
                      oAuthManager.getExpirationDate(tokenResponse.getExpires_in()));
              mealDao.updateUserRefreshToken(user.getUsername(), tokenResponse.getRefresh_token());
              SecurityContextHolder.getContext()
                  .setAuthentication(
                      new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            } catch (Exception e) {
              System.out.println("Refresh token expired, sending to login page");
            }
          }
        } else {
          authenticationService.deleteTokenCookie();
        }
      }
    }
  }

  @Before("execution(* com.sjwi.meals.controller.kroger.*.*(..))")
  public void validateRefreshToken(JoinPoint joinPoint) throws IOException, ParseException {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    String jwtTokenExpiresOn = request.getSession().getAttribute("JWT_EXPIRES_ON").toString();
    if (oAuthManager.hasTokenExpired(jwtTokenExpiresOn)) {
      try {
        System.out.println("Refreshing token");
        MealsUser user =
            (MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccessTokenResponse tokenResponse = oAuthManager.refreshAccessToken(user.getRefreshToken());
        mealDao.updateUserRefreshToken(user.getUsername(), tokenResponse.getRefresh_token());
        request.getSession().setAttribute("JWT", tokenResponse.getAccess_token());
        request
            .getSession()
            .setAttribute(
                "JWT_EXPIRES_ON", oAuthManager.getExpirationDate(tokenResponse.getExpires_in()));
      } catch (Exception e) {
        response.sendRedirect(request.getContextPath() + "/login");
      }
    }
  }
}
