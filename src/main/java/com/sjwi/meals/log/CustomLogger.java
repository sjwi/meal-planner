/* (C)2022 https://stephenky.com */
package com.sjwi.meals.log;

import com.sjwi.meals.model.security.MealsUser;
import eu.bitwalker.useragentutils.UserAgent;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomLogger {

  public static final String LOG_FILE_PROPERTY_KEY = "log.feed";

  Logger log = LoggerFactory.getLogger(CustomLogger.class);

  public void info(String message) {
    log.info(message);
  }

  public void debug(String message) {
    log.debug(message);
  }

  public void error(String message) {
    log.error(message);
  }

  public void logMessageWithEmail(String message) {
    log.info(message);
  }

  public void logUserActionWithEmail(String message) {
    message += " by " + getLoggedInUser();
    log.info(message);
  }

  public void logSignIn(HttpServletRequest request, String user) {
    String msg = user + " logged in on device " + getDeviceType(request);
    log.info(msg);
  }

  public void logSessionCreation(HttpServletRequest request) {
    String msg = "New session created on device " + getDeviceType(request);
    log.info(msg);
  }

  public void logSignInFromCookie(HttpServletRequest request, String user) {
    String msg = user + " logged in on device " + getDeviceType(request) + " by a stored token";
    log.info(msg);
  }

  private String getDeviceType(HttpServletRequest request) {
    String agent = request.getHeader("User-Agent");
    return UserAgent.parseUserAgentString(agent).getOperatingSystem().getDeviceType().toString()
        + " || "
        + UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
  }

  private String getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      MealsUser user =
          (MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return user.getFirstName() != null ? user.getFullName() : user.getUsername();
    }
    return "anonymous user";
  }
}
