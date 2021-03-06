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
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.net.MalformedURLException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/upload-handler")
public class UploadHandlerServlet extends HttpServlet {
  private DatastoreService datastore;
  private Logger logger;
  private UserService userService;

  public UploadHandlerServlet() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
    this.logger = LogManager.getLogger("Error");
    this.userService = UserServiceFactory.getUserService();
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String sender = getSender(request);
    String displayName = getDisplayName(request);
    String comment = getComment(request);
    String imageUrl = getUploadedFileUrl(request, "image");

    // Prevents users from submitting comments with no content."
    if (comment.equals("")) {
        response.setContentType("text/html");
        response.getWriter().println("Please enter a comment");
      return;
    }
    // Store new comment into datastore
    datastore.put(createCommentEntity(sender, displayName, comment, imageUrl));

    response.sendRedirect("/index.jsp");
  }

  // Creates a comment entity given the sender and message
  private Entity createCommentEntity(String sender, String displayName, String comment, String imgUrl) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("displayName", displayName);
    commentEntity.setProperty("sender", sender);
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    commentEntity.setProperty("imgUrl", imgUrl);
    return commentEntity;
  }

  // returns username from optional name input, otherwise uses user's email
  private String getDisplayName(HttpServletRequest request) {
    String nameParam = request.getParameter("name");
    return nameParam.equals("") ? getSender(request) : nameParam;
  }

  // Gets name from form input
  private String getSender(HttpServletRequest request) {
    return userService.getCurrentUser().getEmail();
  }

  // Gets comment from form input 
  private String getComment(HttpServletRequest request) {
    return request.getParameter("comment");
  }

  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }
  
    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    try {
      URL url = new URL(imagesService.getServingUrl(options));
      return url.getPath();
    } catch (MalformedURLException e) {
      return imagesService.getServingUrl(options);
    }
  }
}