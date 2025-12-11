package com.travel.demo.service;

import com.travel.demo.dto.BaseOverViewReportDTO;
import com.travel.demo.dto.OrderStatusReportDTO;
import com.travel.demo.dto.WeeklyRevenueReportDTO;

import java.util.List;

public interface ReportService {
    public BaseOverViewReportDTO overviewOrders(String filter);
    public BaseOverViewReportDTO overviewRevenue(String filter);
    public BaseOverViewReportDTO overviewCustomer(String filter);
    public List<WeeklyRevenueReportDTO> weeklyRevenue();
    public List<OrderStatusReportDTO> orderStatus();
}
