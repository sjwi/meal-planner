package com.sjwi.meals.service.security;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.sjwi.meals.model.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) throws Exception {
    SignatureAlgorithm sa = SignatureAlgorithm.RS256;
    SecretKeySpec secretKeySpec = new SecretKeySpec(DatatypeConverter.parseBase64Binary(clientSecret), sa.getJcaName());
    String[] chunks = response.getAccess_token().split("\\.");
    Base64.Decoder decoder = Base64.getDecoder();
    String header = new String(decoder.decode(chunks[0]));
    String payload = new String(decoder.decode(chunks[1]));
    String tokenWithoutSignature = chunks[0] + "." + chunks[1];
    String signature = chunks[2];
    DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
    if (!validator.isValid(tokenWithoutSignature, signature)) {
      throw new Exception("Could not verify JWT token integrity!");
    }
    return payload;
  }
  
}
