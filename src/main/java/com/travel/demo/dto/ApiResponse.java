package com.travel.demo.dto;

public class ApiResponse {
    String message;
    ResponseLogin data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(ResponseLogin data) {
        this.data = data;
    }
}
