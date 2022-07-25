/* (C)2022 sjwi */
package com.sjwi.meals.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.sjwi.meals.model.kroger.Item;
import com.sjwi.meals.model.kroger.Items;
import com.sjwi.meals.model.kroger.Lists;
import com.sjwi.meals.model.kroger.Lists.List;
import com.sjwi.meals.model.kroger.Locations;
import com.sjwi.meals.model.kroger.Products;

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
  private static final String LISTS_ENDPOINT = "/lists";
  private static final Integer ITEMS_PER_PAGE = 15;

  public Products getProductsByTerm(String name, String locationId, Integer pageCount) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      if (name == null || name.trim().isEmpty()) return new Products();

      Integer start = pageCount == 1 ? 1 : (pageCount - 1) * ITEMS_PER_PAGE;
      String url =
          baseUrl
              + PRODUCTS_ENDPOINT
              + "?filter.locationId="
              + locationId
              + "&filter.term="
              + URLEncoder.encode(name, "UTF-8")
              + "&filter.limit="
              + ITEMS_PER_PAGE
              + "&filter.start="
              + start;

      URI uri = new URI(url);

      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + getSessionToken());
      HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
      ResponseEntity<Products> result =
          restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Products.class);
      return result.getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return new Products();
    }
  }

  public Locations getLocationsByZip(String zipCode) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      if (zipCode == null || zipCode.trim().isEmpty()) return new Locations();

      String url = baseUrl + LOCATIONS_ENDPOINT + "?filter.zipCode.near=" + zipCode;

      URI uri = new URI(url);

      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + getSessionToken());
      HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
      ResponseEntity<Locations> result =
          restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Locations.class);
      return result.getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return new Locations();
    }
  }

  public void addProductToList(String weekName, String upc, int count)
      throws URISyntaxException, UnsupportedEncodingException {
    List list = getListForWeek(weekName);
    RestTemplate restTemplate = new RestTemplate();
    String url = baseUrl + LISTS_ENDPOINT + "/" + list.id;
    URI uri = new URI(url);

    Item[] items = new Item[] {new Item(upc, count)};
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    headers.add("Content-Type", "application/json");
    Map<String, Object> requestObject = Map.of("versionKey", list.versionKey, "items", items);
    HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(requestObject), headers);
    restTemplate.exchange(uri, HttpMethod.PATCH, requestEntity, String.class);
  }

  private List getListForWeek(String weekName) throws URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    String url = baseUrl + LISTS_ENDPOINT;
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
    Map<String, ?> params = Map.of("filter.name", weekName);
    ResponseEntity<Lists> result =
        restTemplate.exchange(url, HttpMethod.GET, requestEntity, Lists.class, params);
    Lists lists = result.getBody();
    if (lists.data.length == 0) return createListForWeek(weekName);
    return result.getBody().data[0];
  }

  private List createListForWeek(String weekName) throws URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    String url = baseUrl + LISTS_ENDPOINT;
    URI uri = new URI(url);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    headers.add("Content-Type", "application/json");
    HttpEntity<String> requestEntity =
        new HttpEntity<>(new Gson().toJson(Map.entry("name", weekName)), headers);
    ResponseEntity<Lists> result =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Lists.class);
    return result.getBody().data[0];
  }

  public void addProductToCart(String upc, int count)
      throws URISyntaxException, UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();

    String url = baseUrl + ADD_CART_ENDPOINT;
    URI uri = new URI(url);

    Items items = new Items(new Item[] {new Item(upc, count)});
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getSessionToken());
    headers.add("Content-Type", "application/json");
    HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(items), headers);
    restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, String.class);
  }

  private String getSessionToken() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    return request.getSession().getAttribute("JWT").toString();
  }
}
