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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private ArrayList<Comment> comments;

  public DataServlet() {
    comments = new ArrayList<Comment>();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String msgJSON = convertToJSON();
    response.setContentType("application/json;");
    response.getWriter().println(msgJSON);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getName(request);
    String comment = getComment(request);
    // Prevents users from submitting comments with no content."
    if (comment.equals("")) {
        response.setContentType("text/html");
        response.getWriter().println("Please enter a comment");
      return;
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(createCommentEntity(name, comment));

    comments.add(new Comment(comment, name));
    response.sendRedirect("/index.html");
  }

  private Entity createCommentEntity(String name, String comment) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("sender", name);
    commentEntity.setProperty("text", comment);
    return commentEntity;
  }
  // Gets name from form input
  private String getName(HttpServletRequest request) {
    String name = request.getParameter("name");
    return name.equals("") ? "Anonymous" : name;
  }

  // Gets comment from form input 
  private String getComment(HttpServletRequest request) {
    return request.getParameter("comment");
  }

  // uses Gson library to convert comments list into json
  private String convertToJSON() {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return "{\"comments\": " + json + "}";
  }

  private void addComment(String comment) {
    this.comments.add(new Comment(comment));
  }
}
