package com.sjwi.meals.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.sjwi.meals.model.Products;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class KrogerService {
  
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

  @Value("${meals.auth.baseUrl}")
  String baseUrl;

  private static final String PRODUCTS_ENDPOINT = "/products";
  private static final String LOCATION_ID = "02400347";
  private static final String LIMIT = "15";

  public Products getProductsByTerm(String name) throws URISyntaxException, UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();
     
    String url = baseUrl + PRODUCTS_ENDPOINT +
      "?filter.term=" + URLEncoder.encode(name, "UTF-8") +
      "&filter.locationId=" + LOCATION_ID +
      "&filter.limit=" + LIMIT;
    URI uri = new URI(url);
    
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + getSessionToken());
    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<Products> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Products.class);
    return result.getBody();
  }

  private String getSessionToken() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    return request.getSession().getAttribute("JWT").toString();
  }

}
