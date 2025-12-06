package com.travel.demo.dto;

public class ApiResponse <T>{
    String message;
     T data; // 👈 Tự động nhận mọi kiểu DTO

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {        // 👈 Trả về đúng kiểu T
        return data;
    }

    public void setData(T data) {   // 👈 Nhận đúng kiểu T
        this.data = data;
    }
}
