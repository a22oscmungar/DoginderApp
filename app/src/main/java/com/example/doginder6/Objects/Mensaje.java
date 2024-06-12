package com.example.doginder6.Objects;

public class Mensaje {
    private int senderId;
    private int receiverId;
    private String message;
    private String timesStamp;

    public Mensaje(int senderId, int receiverId, String message, String timesStamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timesStamp = timesStamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimesStamp() {
        return timesStamp;
    }

    public void setTimesStamp(String timesStamp) {
        this.timesStamp = timesStamp;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", message='" + message + '\'' +
                '}';
    }
}
