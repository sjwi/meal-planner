package com.sjwi.meals.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
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
  private static final String ADD_CART_ENDPOINT = "/cart/add";
  private static final String LOCATION_ID = "02400347";
  private static final String LIMIT = "15";

  public Products getProductsByTerm(String name) throws URISyntaxException, UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();
    String url;
    if (name == null || name.trim().isEmpty()) 
      url = baseUrl + PRODUCTS_ENDPOINT +
        "?filter.locationId=" + LOCATION_ID +
        "&filter.term=" + URLEncoder.encode(name, "UTF-8") +
        "&filter.limit=" + LIMIT;
    else
      url = baseUrl + PRODUCTS_ENDPOINT +
        "?filter.locationId=" + LOCATION_ID +
        "&filter.limit=" + LIMIT;

    URI uri = new URI(url);
    
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<Products> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Products.class);
    return result.getBody();
  }

  public void addProductToCart(String upc, int count) throws URISyntaxException, UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();
     
    String url = baseUrl + ADD_CART_ENDPOINT;
    URI uri = new URI(url);
    
    Items items = new Items();
    items.items = new Item[] {new Item(upc,count)};
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    headers.add("Content-Type", "application/json");
    HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(items), headers);
    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, String.class);
  }

  private String getSessionToken() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    return request.getSession().getAttribute("JWT").toString();
  }

  public class Items {
    public Item[] items;
  }

  public class Item {
    public Item (String upc, int quantity) {
      this.upc = upc;
      this.quantity = quantity;
    }
    public int quantity;
    public String upc;
  }

}
