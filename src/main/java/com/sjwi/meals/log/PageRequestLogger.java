package com.sjwi.meals.log;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.sjwi.meals.dao.MealDao;
import com.sjwi.meals.model.security.MealsUser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import eu.bitwalker.useragentutils.UserAgent;

@Aspect
@Configuration
public class PageRequestLogger {

	@Autowired
	CustomLogger log;

	@Autowired
	MealDao mealDao;
	
	@Before("execution(* com.sjwi.meals.controller.kroger.*.*(..))" + 
  "|| execution(* com.sjwi.meals.controller.HomeController.*(..))" +
	"|| execution(* com.sjwi.meals.controller.LifecycleController.*(..))")
	public void logPageRequest(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String signature = joinPoint.getSignature().toShortString();
		String requestUrl = request.getRequestURL().toString();
		String parameters = request.getParameterMap().entrySet().stream()
				.map(p -> "[" + p.getKey() + ": " + String.join(",", request.getParameterMap().get(p.getKey())) + "],").collect(Collectors.joining());
		String username = getLoggedInUser();
		String os =  getOs();

		log.info(signature + "\n\t" + requestUrl + "\n\t\t" + parameters + "\n\tcalled by " + username + " on a " + os + " device.\n\n");
		mealDao.log(username,os,signature,requestUrl,parameters);
	}

	private String getLoggedInUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
        MealsUser user = (MealsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				return user.getFirstName() != null? user.getFullName(): user.getUsername();
		}
		return "anonymous user";
	}

	private String getOs() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String agent = request.getHeader("User-Agent");
		return UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
	}
	
}
