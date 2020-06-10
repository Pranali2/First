package com.example.first.Model_Hope;

public class Chat_Hope {
    private String sender;
    private String receiver;
    private String message;
    private boolean issen;

    public Chat_Hope(String sender, String receiver, String message,boolean issen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.issen=issen;
    }
    public Chat_Hope(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIssen() {
        return issen;
    }

    public void setIssen(boolean issen) {
        this.issen = issen;
    }
}
