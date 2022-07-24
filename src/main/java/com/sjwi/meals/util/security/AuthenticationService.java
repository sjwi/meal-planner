/* (C)2022 sjwi */
package com.sjwi.meals.util.security;

import com.sjwi.meals.dao.MealDao;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationService {

  public static final String STORED_COOKIE_TOKEN_KEY = "USESSIONID";

  @Autowired MealDao mealDao;

  @Autowired ServletContext context;

  public void generateCookieToken(String username) {
    System.out.println("Generating cookie token");
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    String token = generateTokenString();
    mealDao.storeCookieToken(username, token);
    response.addCookie(buildStaticCookie(STORED_COOKIE_TOKEN_KEY, token));
  }

  private String generateTokenString() {
    Random random = ThreadLocalRandom.current();
    byte[] r = new byte[256]; // Means 2048 bit
    random.nextBytes(r);
    return Base64.encodeBase64String(r);
  }

  public Cookie buildStaticCookie(String key, String setting) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String host = request.getServerName();
    Cookie cookie = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null && Arrays.stream(cookies).anyMatch(c -> c.getName().equals(key))) {
      cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(key)).findFirst().orElse(null);
      cookie.setValue(setting);
    } else {
      cookie = new Cookie(key, setting);
    }
    cookie.setMaxAge(60 * 60 * 24 * 7 * 52 * 10);
    cookie.setDomain(host);
    cookie.setPath(context.getContextPath().isEmpty() ? "/" : context.getContextPath());
    return cookie;
  }

  public void deleteCookieToken(Cookie cookieToken) {
    if (cookieToken != null) {
      mealDao.deleteCookieToken(cookieToken.getValue());
    }
  }

  public boolean userHasLoginCookie() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return false;
    System.out.println("Inspecting cookies");
    return Arrays.stream(cookies)
        .peek(c -> System.out.println(c))
        .anyMatch(
            c ->
                c.getName() != null
                    && c.getName().equals(STORED_COOKIE_TOKEN_KEY)
                    && c.getPath()
                        .equals(
                            context.getContextPath().isEmpty() ? "/" : context.getContextPath()));
  }

  public void deleteTokenCookie() {
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    Cookie cookie = buildStaticCookie(STORED_COOKIE_TOKEN_KEY, "");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}
