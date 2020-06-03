package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.sps.data.LoginStatus;

@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {
  UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String statusJson = convertToJSON(userService.isUserLoggedIn());
    response.setContentType("application/json");
    response.getWriter().println(statusJson);
  }

  private String convertToJSON(boolean status) {
    Gson gson = new Gson();
    LoginStatus loginStats;
    if (status) {
      loginStats = new LoginStatus(status, userService.createLogoutURL("/index.jsp"));
    } else {
      loginStats = new LoginStatus(status, userService.createLoginURL("/index.jsp"));
    }
    return gson.toJson(loginStats);
  }
}