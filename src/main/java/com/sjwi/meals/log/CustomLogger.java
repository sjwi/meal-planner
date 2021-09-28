package com.sjwi.meals.log;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.sjwi.meals.model.security.MealsUser;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class CustomLogger {
	
	public static final String LOG_FILE_PROPERTY_KEY = "log.feed";

	private Logger log;

	@PostConstruct
	public void initialize() {
		System.setProperty(LOG_FILE_PROPERTY_KEY,System.getenv("CATALINA_BASE") + "/logs/meals");
		log = Logger.getLogger(CustomLogger.class.getName());
	}
	public void info(Object message) {
		log.info(message);
	}
	
	public void debug(Object message) {
		log.debug(message);
	}

	public void error(Object message) {
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
		String msg =  "New session created on device " + getDeviceType(request);
		log.info(msg);
	}
	public void logSignInFromCookie(HttpServletRequest request, String user) {
		String msg = user + " logged in on device " + getDeviceType(request) + " by a stored token";
		log.info(msg);
	}
	private String getDeviceType(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");	
		return UserAgent.parseUserAgentString(agent).getOperatingSystem().getDeviceType().toString() + " || "
				+ UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
	}

	private String getLoggedInUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
        MealsUser user = (MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				return user.getFirstName() != null? user.getFullName(): user.getUsername();
		}
		return "anonymous user";
	}
	
}
