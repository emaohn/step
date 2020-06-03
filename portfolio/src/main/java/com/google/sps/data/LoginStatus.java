package com.google.sps.data;

public class LoginStatus {
  private boolean isLoggedIn;
  private String url;

  public LoginStatus(boolean status, String url) {
    this.isLoggedIn = status;
    this.url = url;
  }

  public boolean getIsLogged() {
    return this.isLoggedIn;
  }

  public String getUrl() {
    return this.url;
  }
}