package com.travel.demo.service;

import com.travel.demo.dto.*;

import java.util.List;

public interface StatisticService {
    StatisticDTO kpis(int range);
    List<SeriesPointDTO> revenueSeries(int range);
    List<OrderStatusReportDTO> orderStatus(int range);
    CustomerVipDTO customerVip(int range);
    List<SeriesPointDTO> customerSeries(int range);

    DashboardDTO dashboard(int range);

    DashboardDTO dailyReport(); // dùng cho email
}
