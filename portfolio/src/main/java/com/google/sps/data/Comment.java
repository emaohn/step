package com.google.sps.data;

/*
 * Comment class stores message and sender.
 */

 public class Comment {
    private String sender;
    private String text;
    private long id;
    private long timestamp;
    private String imgURL;

    public Comment(String text, String sender, long id, long timestamp, String img) {
      this.sender = sender;
      this.text = text;
      this.id = id;
      this.timestamp = timestamp;
      this.imgURL = img;
    }

    public String getSender() {
      return this.sender;
    }
     
    public String getText() {
      return this.text;
    }

    public long getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
 }
