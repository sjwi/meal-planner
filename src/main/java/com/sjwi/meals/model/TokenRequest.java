package com.sjwi.meals.model;

public class TokenRequest {
  private String code;
  private String grant_type = "authorization_code";
  private String clientId;
  private String redirectUrl;

  public TokenRequest(String code, String clientId, String redirectUrl) {
    this.setCode(code);
    this.setClientId(clientId);
    this.setRedirectUrl(redirectUrl);
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getGrant_type() {
    return grant_type;
  }

  public void setGrant_type(String grant_type) {
    this.grant_type = grant_type;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
