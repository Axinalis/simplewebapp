package com.mastery.java.task.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMessage {

    private String message;
    private Map<String, String> details;
    private String status;

    public ExceptionMessage(String message) {
        this.details = new HashMap<>();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}