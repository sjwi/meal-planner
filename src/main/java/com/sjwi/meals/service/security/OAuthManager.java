package com.sjwi.meals.service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

  public String getAuthCode() {
    return null;
  }

  public String getOAuthToken(String code) {
    ResponseEntity<String> response = null;
    System.out.println("Authorization Code------" + code);

    RestTemplate restTemplate = new RestTemplate();

    String credentials = "javainuse:secret";
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Authorization", "Basic " + encodedCredentials);

    HttpEntity<String> request = new HttpEntity<String>(headers);

    String access_token_url = "http://localhost:8080/oauth/token";
    access_token_url += "?code=" + code;
    access_token_url += "&grant_type=authorization_code";
    access_token_url += "&redirect_uri=http://localhost:8090/showEmployees";
    response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
    return response.getBody();
  }

}
