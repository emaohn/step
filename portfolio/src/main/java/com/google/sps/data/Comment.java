package com.google.sps.data;

/*
 * Comment class stores message and sender.
 */

 public class Comment {
    private String sender;
    private String text;

    public Comment(String text, String sender) {
      this.sender = sender;
      this.text = text;
    }

    public String getSender() {
      return this.sender;
    }
     
    public String getText() {
      return this.text;
    }
 }
