package com.sjwi.meals.service.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.spec.SecretKeySpec;

import com.sjwi.meals.model.AccessTokenResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) throws NoSuchAlgorithmException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, InvalidKeySpecException {
    SignatureAlgorithm sa = SignatureAlgorithm.RS256;
    SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(clientSecret), sa.getJcaName());
    return Jwts.parser().setSigningKey(secretKeySpec).parseClaimsJws(response.getAccess_token()).getBody().toString();
  }
  
}
