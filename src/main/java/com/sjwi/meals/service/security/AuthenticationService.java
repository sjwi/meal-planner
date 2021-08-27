package com.sjwi.meals.service.security;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.meals.dao.MealDao;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

  public static final String STORED_COOKIE_TOKEN_KEY = "USESSIONID";

  @Autowired
  MealDao mealDao;

  @Autowired
  ServletContext context;

  public void generateCookieToken(HttpServletRequest request, HttpServletResponse response, String username) {
    String token = generateTokenString();
    mealDao.storeCookieToken(username,token);
    response.addCookie(buildStaticCookie(request.getServerName(), STORED_COOKIE_TOKEN_KEY, token, request.getCookies()));
  }

  private String generateTokenString(){
    Random random = ThreadLocalRandom.current();
    byte[] r = new byte[256]; //Means 2048 bit
    random.nextBytes(r);
    return Base64.encodeBase64String(r);
  }

  public Cookie buildStaticCookie(String host, String key, String setting, Cookie[] cookies) {
		Cookie cookie = null;
		if (cookies != null && Arrays.stream(cookies).anyMatch(c -> c.getName().equals(key))) {
			cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(key)).findFirst().orElse(null);
			cookie.setValue(setting);
		} else {
			cookie = new Cookie(key,setting);
		}
		cookie.setMaxAge(60 * 60 * 24 * 7 * 52 * 10);
		cookie.setDomain(host);
    cookie.setPath(context.getContextPath());
		return cookie;
	}

  public void deleteCookieToken(Cookie cookieToken) {
    if (cookieToken != null) {
			mealDao.deleteCookieToken(cookieToken.getValue());
		}
  }
}
