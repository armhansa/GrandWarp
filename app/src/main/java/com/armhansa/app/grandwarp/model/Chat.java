package com.armhansa.app.grandwarp.model;

import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private Date time;
    private String message;

    public Chat(String sender, String receiver, Date time, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

}
