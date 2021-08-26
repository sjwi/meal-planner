package com.sjwi.meals.service.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Component
public class OAuthManager {

  @Value("${meals.auth.auth_url}")
  String authUrl;

  @Value("${meals.auth.access_token_url}")
  String tokenUrl;

  @Value("${meals.auth.client_id}")
  String clientId;

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  @Value("${meals.auth.redirect_uri}")
  String redirectUri;

  @Value("${meals.auth.scopes}")
  String scopes;

  public String getOAuthToken(String code) {
    ResponseEntity<String> response = null;
    System.out.println("Authorization Code------" + code);

    RestTemplate restTemplate = new RestTemplate();

    String credentials = clientId +  ":" + clientSecret;
    System.out.println(credentials);
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.ALL));
    headers.add("Authorization", "Basic " + encodedCredentials);

    HttpEntity<String> request = new HttpEntity<String>(headers);

    String access_token_url = tokenUrl;
    access_token_url += "?code=" + code;
    access_token_url += "&grant_type=authorization_code";
    access_token_url += "&redirect_uri=" + redirectUri;
    access_token_url += "&client_id=" + clientId;
    response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
    return response.getBody();
  }

}
