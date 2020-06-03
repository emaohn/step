package com.google.sps.data;

public class LoginStatus {
  private boolean status;
  private String url;

  public LoginStatus(boolean status, String url) {
    this.status = status;
    this.url = url;
  }

  public boolean getStatus() {
    return this.status;
  }

  public String getUrl() {
    return this.url;
  }
}