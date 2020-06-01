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

@WebServlet("/upload-handler")
public class UploadHandlerServlet extends HttpServlet {
  private DatastoreService datastore;
  private Logger logger;

  public UploadHandlerServlet() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
    this.logger = LogManager.getLogger("Error");
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getName(request);
    String comment = getComment(request);
    String imageUrl = getUploadedFileUrl(request, "image");

    // Prevents users from submitting comments with no content."
    if (comment.equals("")) {
        response.setContentType("text/html");
        response.getWriter().println("Please enter a comment");
      return;
    }
    // Store new comment into datastore
    datastore.put(createCommentEntity(name, comment));

    response.sendRedirect("/index.html");
  }

  // Creates a comment entity given the sender and message
  private Entity createCommentEntity(String name, String comment) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("sender", name);
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
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