package com.sjwi.meals.service.security;

import com.sjwi.meals.model.AccessTokenResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) {
    return Jwts.parser().setSigningKey(Base64.decodeBase64(clientSecret)).parseClaimsJws(response.getAccess_token()).getBody().toString();
  }
  
}
