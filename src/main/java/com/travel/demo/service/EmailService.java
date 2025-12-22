package com.travel.demo.service;

import com.travel.demo.dto.StatisticDTO;

public interface EmailService {
    void sendResetPasswordEmail(String to, String newPassword);
    void sendCreateAccount(String to, String newPassword);
    void sendStatisticReportEmail(String to, StatisticDTO kpi, int range);

}
