package com.acme.utils.models;

import camundajar.impl.com.google.gson.annotations.Expose;

public class Result {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    @Expose
    private String status;

    @Expose
    private String message;

    public Result(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
