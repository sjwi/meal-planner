package com.sjwi.meals.service.security;

import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import java.io.UnsupportedEncodingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

  public String getOAuthToken(String code) throws UnsupportedEncodingException{
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
    access_token_url += "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8");
    access_token_url += "&client_id=" + clientId;
    response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
    return response.getBody();
  }

}
