package com.sjwi.meals.service.security;

import java.security.interfaces.RSAPublicKey;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sjwi.meals.model.security.AccessTokenResponse;

import org.apache.commons.codec.binary.Base64;

public class JwtManager {
  
  private static final String KROGER_BASE_URI = "https://api.kroger.com/v1";

  private DecodedJWT jwt;

  public JwtManager(AccessTokenResponse response) throws JwkException{
    DecodedJWT jwt = JWT.decode(response.getAccess_token());
    JwkProvider provider = new UrlJwkProvider(KROGER_BASE_URI);
    Jwk jwk = provider.get(jwt.getKeyId());
    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
    algorithm.verify(jwt);
    this.jwt = jwt;
  }

  public String getUnpackedAccessToken() {
    return Base64.decodeBase64(jwt.getPayload()).toString();
  }
  public String getOAuthUser() {
    return jwt.getSubject();
  }
}
