/* (C)2022 https://stephenky.com */
package com.sjwi.meals.util.security;

import com.google.gson.Gson;
import com.sjwi.meals.model.security.AccessTokenResponse;
import com.sjwi.meals.service.ParameterStringBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

  private static final String STRING_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  public AccessTokenResponse getOAuthToken(String code) throws Exception {

    ResponseEntity<String> response = null;

    RestTemplate restTemplate = new RestTemplate();

    String credentials = clientId + ":" + clientSecret;
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.ALL));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("Authorization", "Basic " + encodedCredentials);

    Map<String, String> params = new HashMap<>();
    params.put("client_id", clientId);
    params.put("redirect_uri", redirectUri);
    params.put("code", code);
    params.put("grant_type", "authorization_code");

    HttpEntity<String> request =
        new HttpEntity<String>(ParameterStringBuilder.getParamsString(params), headers);
    response = restTemplate.postForEntity(tokenUrl, request, String.class);
    return new Gson().fromJson(response.getBody(), AccessTokenResponse.class);
  }

  public String getSignOnUrl() {
    return authUrl
        + "?response_type=code&client_id="
        + clientId
        + "&redirect_uri="
        + redirectUri
        + "&scope="
        + scopes.replaceAll("&", " ")
        + "&state=STATE";
  }

  public String getExpirationDate(int expiresIn) {
    Calendar date = Calendar.getInstance();
    long timeInSecs = date.getTimeInMillis();
    Date futureDate = new Date(timeInSecs + (expiresIn * 1000));
    SimpleDateFormat outputFormat = new SimpleDateFormat(STRING_FORMAT);
    return outputFormat.format(futureDate);
  }

  public boolean hasTokenExpired(String expiresIn) throws ParseException {
    SimpleDateFormat inputFormat = new SimpleDateFormat(STRING_FORMAT);
    Date expiresOn = inputFormat.parse(expiresIn);
    Date now = Calendar.getInstance().getTime();
    return now.compareTo(expiresOn) > 0;
  }

  public AccessTokenResponse refreshAccessToken(String refreshToken) {
    ResponseEntity<String> response = null;
    RestTemplate restTemplate = new RestTemplate();

    String credentials = clientId + ":" + clientSecret;
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.ALL));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("Authorization", "Basic " + encodedCredentials);

    Map<String, String> params = new HashMap<>();
    params.put("grant_type", "refresh_token");
    params.put("refresh_token", refreshToken);

    HttpEntity<String> request =
        new HttpEntity<String>(ParameterStringBuilder.getParamsString(params), headers);
    response = restTemplate.postForEntity(tokenUrl, request, String.class);
    return new Gson().fromJson(response.getBody(), AccessTokenResponse.class);
  }
}
