/* (C)2022 https://stephenky.com */
package com.sjwi.meals.service;

import com.sjwi.meals.dao.MealDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

  @Autowired MealDao mealDao;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return mealDao.getUser(username.trim().toLowerCase());
  }

  public void updateAccount(String username, String firstName, String lastName, String email) {
    mealDao.updateUser(username, firstName, lastName, email);
    refreshUserState(username);
  }

  public void refreshUserState(String username) {
    UserDetails userDetails = loadUserByUsername(username);
    Authentication newAuth =
        new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(newAuth);
  }

  public void deleteAccount(String name) {
    mealDao.deleteAccount(name);
    mealDao.deleteUserWeeks(name);
    mealDao.deleteUserIngredients(name);
    mealDao.deleteUserTags(name);
    mealDao.deleteUserSides(name);
    mealDao.deleteUserMeals(name);
    SecurityContextHolder.clearContext();
  }
}
