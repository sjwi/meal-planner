package com.sjwi.meals.service.security;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.sjwi.meals.model.security.AccessTokenResponse;
import com.sjwi.meals.model.security.JwtHeader;
import com.sjwi.meals.model.security.WellKnownConfig;
import com.sjwi.meals.model.security.WellKnownConfig.ConfigKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) throws Exception {
    DecodedJWT jwt = JWT.decode(response.getAccess_token());
    JwkProvider provider = new UrlJwkProvider("https://api.kroger.com/v1");
    Jwk jwk = provider.get(jwt.getKeyId());
    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
    algorithm.verify(jwt);
    return jwt.getPayload();
  }

  private String getPrivateKey(JwtHeader jwtHeader) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
     
    final String urlEndpoint = jwtHeader.getJku();
    URI uri = new URI(urlEndpoint);
    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    WellKnownConfig config = new Gson().fromJson(result.getBody(), WellKnownConfig.class);
    ConfigKey key = Arrays.stream(config.keys).filter(k -> k.kid.equals(jwtHeader.getKid())).findFirst().orElse(null);
    if (key == null)
      throw new Exception("No private key found");
    return key.n;
  }
  
}
