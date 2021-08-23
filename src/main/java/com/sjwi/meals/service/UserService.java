package com.sjwi.meals.service;

import com.sjwi.meals.dao.MealDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
	
	@Autowired
	MealDao mealDao;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
	    return mealDao.getUser(username.trim().toLowerCase());
	}
}
	
