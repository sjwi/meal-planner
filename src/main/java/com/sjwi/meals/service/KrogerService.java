package com.sjwi.meals.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.sjwi.meals.model.kroger.Item;
import com.sjwi.meals.model.kroger.Items;
import com.sjwi.meals.model.kroger.Locations;
import com.sjwi.meals.model.kroger.Products;

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
  private static final String LOCATIONS_ENDPOINT = "/locations";
  private static final String ADD_CART_ENDPOINT = "/cart/add";
  private static final String LIMIT = "15";

  public Products getProductsByTerm(String name, String locationId) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      if (name == null || name.trim().isEmpty()) 
        return new Products();

      String url = baseUrl + PRODUCTS_ENDPOINT +
          "?filter.locationId=" + locationId +
          "&filter.term=" + URLEncoder.encode(name, "UTF-8") +
          "&filter.limit=" + LIMIT;

      URI uri = new URI(url);
      
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + getSessionToken());
      HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
      ResponseEntity<Products> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Products.class);
      return result.getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return new Products();
    }
  }

  public Locations getLocationsByZip(String zipCode) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      if (zipCode == null || zipCode.trim().isEmpty()) 
        return new Locations();

      String url = baseUrl + LOCATIONS_ENDPOINT + "?filter.zipCode.near=" + zipCode;

      URI uri = new URI(url);
      
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + getSessionToken());
      HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
      ResponseEntity<Locations> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Locations.class);
      return result.getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return new Locations();
    }
  }

  public void addProductToCart(String upc, int count) throws URISyntaxException, UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();
     
    String url = baseUrl + ADD_CART_ENDPOINT;
    URI uri = new URI(url);
    
    Items items = new Items(new Item[] {new Item(upc,count)});
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    headers.add("Content-Type", "application/json");
    HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(items), headers);
    restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, String.class);
  }

  private String getSessionToken() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    return request.getSession().getAttribute("JWT").toString();
  }

}
