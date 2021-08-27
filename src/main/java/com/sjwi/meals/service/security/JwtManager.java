package com.sjwi.meals.service.security;

import javax.xml.bind.DatatypeConverter;

import com.sjwi.meals.model.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(clientSecret)).parseClaimsJws(response.getAccess_token()).getBody().toString();
  }
  
}
