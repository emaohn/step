package com.google.sps.data;

/*
 * Comment class stores message and sender.
 */

 public class Comment {
    private String sender;
    private String text;
    private long id;
    private long timestamp;

    public Comment(String text, String sender, long id, long timestamp) {
      this.sender = sender;
      this.text = text;
      this.id = id;
      this.timestamp = timestamp;
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
