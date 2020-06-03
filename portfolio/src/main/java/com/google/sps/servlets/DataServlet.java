// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private DatastoreService datastore;
  private Logger logger;
  private UserService userService;

  public DataServlet() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
    this.logger = LogManager.getLogger("Error");
    this.userService = UserServiceFactory.getUserService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String msgJSON = convertToJSON(getComments(getRequestNum(request), prefersDescending(request)));
    response.setContentType("application/json;");
    response.getWriter().println(msgJSON);
  }


  // Retrieves comments from datastore and converts them into Comment objects.
  private ArrayList<Comment> getComments(int numRequests, boolean prefersDescending) {
    Query query = new Query("Comment");
    if (prefersDescending) {
      query = query.addSort("timestamp", SortDirection.DESCENDING);
    } else {
      query = query.addSort("timestamp", SortDirection.ASCENDING);
    }
    PreparedQuery results = datastore.prepare(query);
    Iterator<Entity> resultsItr = results.asIterable().iterator();
    ArrayList<Comment> comments = new ArrayList<Comment>();
    // Only returns numRequests amount of comments
    int i = 0;
    String userId = userService.isUserLoggedIn() ? userService.getCurrentUser().getEmail() : null;
    while (resultsItr.hasNext() && i < numRequests) {
      Entity curEntity = resultsItr.next();
      String sender = (String) curEntity.getProperty("sender");
      // Display name will be "I" if the current user made the comment
      String name = userId != null && sender.equals(userId) ? "I" : (String) curEntity.getProperty("displayName");
      String text = (String) curEntity.getProperty("text");
      long timestamp = (long) curEntity.getProperty("timestamp");
      String imgUrl = (String) curEntity.getProperty("imgUrl");
      comments.add(new Comment(text, name, curEntity.getKey().getId(), timestamp, imgUrl));
      i++;
    }

    return comments;
  }

  //Get request number from query string
  private int getRequestNum(HttpServletRequest request) {
    String requestString = request.getParameter("request");
    int requestNum;
    try {
      requestNum = Integer.parseInt(requestString);
    } catch (NumberFormatException e) {
      logger.error("Could not convert to int: " + requestString);
      return 10;
    }
    return requestNum;
  }

  //Get sorting preference from query string
  private boolean prefersDescending(HttpServletRequest request) {
    String preference = request.getParameter("sorting");
    return preference.equals("newest") ? true : false;
  }

  // uses Gson library to convert comments list into json string
  private String convertToJSON(ArrayList<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return "{\"comments\": " + json + "}";
  }
}
