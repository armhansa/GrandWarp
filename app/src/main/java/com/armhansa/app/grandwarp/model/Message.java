package com.armhansa.app.grandwarp.model;

import java.util.Date;

public class Message {

    private String sender;
    private Date time;
    private String message;

    public Message(String sender, Date time, String message) {
        this.sender = sender;
        this.time = time;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

}
