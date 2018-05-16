package com.massacre.massacre;

import java.util.Date;

/**
 * Created by saurabh on 8/5/16.
 */
public class Message {

    public Date time;
    public String recipient;
    public int type;
    public String message;
    public int sendOrReceived;
    public int messageId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public int getSendOrReceived() {
        return sendOrReceived;
    }

    public void setSendOrReceived(int sendOrReceived) {
        this.sendOrReceived = sendOrReceived;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
