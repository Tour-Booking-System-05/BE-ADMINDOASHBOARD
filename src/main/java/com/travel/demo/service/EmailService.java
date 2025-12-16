package com.travel.demo.service;

public interface EmailService {
    void sendResetPasswordEmail(String to, String newPassword);
    void sendCreateAccount(String to, String newPassword);

}
