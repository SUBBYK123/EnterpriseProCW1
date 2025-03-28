package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

public class LogModel {
    private int logId;
    private String emailAddress;
    private String action;
    private String status;
    private LocalDateTime timeStamp;



    public LogModel(String emailAddress, String action, String status){
        this.emailAddress = emailAddress;
        this.action = action;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }

    public LogModel(int logId, String emailAddress, String action, String status, LocalDateTime timestamp) {
        this.logId = logId;
        this.emailAddress = emailAddress;
        this.action = action;
        this.status = status;
        this.timeStamp = timestamp;
    }


    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
