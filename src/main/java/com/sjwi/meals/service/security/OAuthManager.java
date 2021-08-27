package com.sjwi.meals.service.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.sjwi.meals.model.TokenRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tomcat.util.codec.binary.Base64;
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

  public String getOAuthToken(String code) throws Exception {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpPost postRequest = new HttpPost(tokenUrl);

    TokenRequest payload = new TokenRequest(code,clientId,redirectUri);
    String payloadString = new Gson().toJson(payload);
    StringEntity input = new StringEntity(payloadString);
    input.setContentType("application/json");
    postRequest.setEntity(input);

    String credentials = clientId +  ":" + clientSecret;
    System.out.println(credentials);
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
    //headers.setAccept(Arrays.asList(MediaType.ALL));
    // headers.setContentType(MediaType.APPLICATION_JSON);
    postRequest.setHeader("Authorization", "Basic " + encodedCredentials);
    postRequest.setHeader("Accept", "*/*");
    postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

    HttpResponse response = httpClient.execute(postRequest);

    if (response.getStatusLine().getStatusCode() != 201) {
        throw new RuntimeException("Failed : HTTP error code : "
            + response.getStatusLine().getStatusCode());
    }

    BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

    String output;
    while ((output = br.readLine()) != null) {
       output += output; 
    }

    httpClient.getConnectionManager().shutdown();

    return output;








    // ResponseEntity<String> response = null;
    // System.out.println("Authorization Code------" + code);

    // RestTemplate restTemplate = new RestTemplate();
    // restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    // String credentials = clientId +  ":" + clientSecret;
    // System.out.println(credentials);
    // String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    // HttpHeaders headers = new HttpHeaders();
    // headers.setAccept(Arrays.asList(MediaType.ALL));
    // headers.setContentType(MediaType.APPLICATION_JSON);
    // headers.add("Authorization", "Basic " + encodedCredentials);


    // TokenRequest payload = new TokenRequest(code,clientId,redirectUri);
    // new Gson().toJson(payload);

    // HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(payload),headers);
    // response = restTemplate.postForEntity(tokenUrl, request, String.class);
    // return response.getBody();
  }

  // public class RestTemplateStandardCookieCustomizer implements RestTemplateCustomizer {
  //   @Override
  //   public void customize(final RestTemplate restTemplate) {
  //     final HttpClient httpClient = HttpClients.custom()
  //         .setDefaultRequestConfig(RequestConfig.custom()
  //             .setCookieSpec(CookieSpecs.STANDARD).build())
  //         .build();

  //     restTemplate.setRequestFactory(
  //       new HttpComponentsClientHttpRequestFactory(httpClient)
  //     );
  //   }
  // }
}
