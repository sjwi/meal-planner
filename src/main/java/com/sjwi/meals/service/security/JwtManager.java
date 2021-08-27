package com.sjwi.meals.service.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import com.sjwi.meals.model.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) throws NoSuchAlgorithmException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, InvalidKeySpecException {
    System.out.println(clientSecret);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec privKeySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(clientSecret));
    return Jwts.parser().setSigningKey(kf.generatePrivate(privKeySpecPKCS8)).parseClaimsJws(response.getAccess_token()).getBody().toString();
  }
  
}
