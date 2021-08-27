package com.sjwi.meals.service.security;

import java.net.URI;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.sjwi.meals.model.security.AccessTokenResponse;
import com.sjwi.meals.model.security.JwtHeader;
import com.sjwi.meals.model.security.WellKnownConfig;
import com.sjwi.meals.model.security.WellKnownConfig.ConfigKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.Jwts;

@Component
public class JwtManager {

  @Value("${meals.auth.client_secret}")
  String clientSecret;

  public String getUnpackedAccessToken(AccessTokenResponse response) throws Exception {
    String[] chunks = response.getAccess_token().split("\\.");
    Base64.Decoder decoder = Base64.getDecoder();
    String header = new String(decoder.decode(chunks[0]));
    JwtHeader jwtHeader = new Gson().fromJson(header, JwtHeader.class);
    String payload = new String(decoder.decode(chunks[1]));
    String privateKey = getPrivateKey(jwtHeader);


    byte[] keyBytes = DatatypeConverter.parseBase64Binary(privateKey); // your key here

    X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);
    return Jwts.parser()
      .setSigningKey(pubKey)
      .parseClaimsJws(response.getAccess_token()).getBody().toString();
 
 
    // String[] chunks = response.getAccess_token().split("\\.");
    // Base64.Decoder decoder = Base64.getDecoder();
    // String header = new String(decoder.decode(chunks[0]));
    // JwtHeader jwtHeader = new Gson().fromJson(header, JwtHeader.class);
    // String privateKey = getPrivateKey(jwtHeader);
    // String payload = new String(decoder.decode(chunks[1]));
    // String tokenWithoutSignature = chunks[0] + "." + chunks[1];
    // String signature = chunks[2];
    // SignatureAlgorithm sa = SignatureAlgorithm.RS256;
    // System.out.println(privateKey);
    // SecretKeySpec secretKeySpec = new SecretKeySpec(privateKey.getBytes(), sa.getJcaName());
    // DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
    // if (!validator.isValid(tokenWithoutSignature, signature)) {
    //   throw new Exception("Could not verify JWT token integrity!");
    // }
    // return payload;
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
