package com.sjwi.meals.model.security;

public class JwtHeader {
  private String alg;
  private String jku;
  private String kid;
  private String type;
  public JwtHeader(String alg, String jku, String kid, String type) {
    this.setAlg(alg);
    this.setJku(jku);
    this.setKid(kid);
    this.setType(type);
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getKid() {
    return kid;
  }
  public void setKid(String kid) {
    this.kid = kid;
  }
  public String getJku() {
    return jku;
  }
  public void setJku(String jku) {
    this.jku = jku;
  }
  public String getAlg() {
    return alg;
  }
  public void setAlg(String alg) {
    this.alg = alg;
  }
}
