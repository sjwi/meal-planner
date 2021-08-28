package com.sjwi.meals.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.meals.service.security.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RequestCache requestCache() {
		return new HttpSessionRequestCache();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/login").permitAll().antMatchers("/images/meals.png").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/oauth2/login").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/images/**").permitAll()
				// .antMatchers("/**").permitAll()
				.antMatchers("/**").access("hasAuthority('USER')")
				.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
				.and().requestCache().requestCache(requestCache())
				.and().logout()
        	.deleteCookies(com.sjwi.meals.service.security.AuthenticationService.STORED_COOKIE_TOKEN_KEY)
        	.logoutSuccessHandler(new CustomLogoutSuccessHandler())
				.and().headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
		;
		httpSecurity.csrf().disable(); // Required for AJAX requests to be authorized
	}


	private class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
		@Autowired
		private AuthenticationService authenticationService;
		@Override
		public void onLogoutSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			String tokenKey = com.sjwi.meals.service.security.AuthenticationService.STORED_COOKIE_TOKEN_KEY;
			if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(c -> tokenKey.equals(c.getName()))) {
				try {
					authenticationService.deleteCookieToken(Arrays.stream(request.getCookies()).filter(c -> tokenKey.equals(c.getName())).findFirst().orElse(null));
				} catch (Exception e ) {}
			}
			response.setStatus(HttpStatus.OK.value());
			response.sendRedirect(request.getContextPath() + "/");
		}
	}
}
	