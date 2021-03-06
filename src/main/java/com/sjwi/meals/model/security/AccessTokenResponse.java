/* (C)2022 https://stephenky.com */
package com.sjwi.meals.model.security;

public class AccessTokenResponse {
  private String refresh_token;
  private int expires_in;
  private String access_token;
  private String token_type;

  public AccessTokenResponse(
      String refresh_token, int expires_in, String access_token, String token_type) {
    this.setRefresh_token(refresh_token);
    this.setExpires_in(expires_in);
    this.setAccess_token(access_token);
    this.setToken_type(token_type);
  }

  public String getToken_type() {
    return token_type;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }
}
