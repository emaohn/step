package com.google.sps.data;

/*
 * Comment class stores message and sender.
 */

 public class Comment {
    private String sender;
    private String text;
    private long id;

    public Comment(String text, String sender, long id) {
      this.sender = sender;
      this.text = text;
      this.id = id;
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
 }
