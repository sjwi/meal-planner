package com.sjwi.meals.model.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MealsUser extends User {
	/**
	 * 
	 */
	public static final Set<String> USER_PREFERENCE_KEYS = new HashSet<>(Arrays.asList("sort", "sortDirection", "pinFavorites","weekStartDay","krogerLocationId"));
	private static final long serialVersionUID = -8215868281708962565L;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String refreshToken;
	private final String fullName;
	private final Map<String, String> preferences;
	
	public MealsUser(String username, String firstName, String lastName, String email, String refreshToken,
			Collection<? extends GrantedAuthority> authorities, Map<String, String> preferences) {
		super(username,"",true,true,true,true,authorities);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.refreshToken = refreshToken;
		this.fullName = firstName + " " + lastName;
		this.preferences = preferences;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getFullName() {
		return fullName;
	}
	public Map<String, String> getPreferences(){
		return preferences;
	}
}
