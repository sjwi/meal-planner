package com.sjwi.meals.model.security;

public class WellKnownConfig {
  public ConfigKey[] keys;
  
  public class ConfigKey {
    public String kid;
    public String alg;
    public String kty;
    public String use;
    public String[] key_ops; 
    public String n;
    public String e;
  }
}
