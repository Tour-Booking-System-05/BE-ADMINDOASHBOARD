package com.travel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDTO {
    private long revenue;          // tổng doanh thu
    private long orders;           // tổng đơn
    private long customers;        // tổng khách hàng (new hoặc distinct tuỳ bạn)
    private double cancelRate;     // 0..1

    private Double revenueChangePct; //Phần trăm thay đổi doanh thu so với kỳ trước
    private Double ordersChangePct; //Phần trăm thay đổi số lượng đơn đặt
    private Double customersChangePct; //Phần trăm thay đổi số khách hàng
    private Double cancelRateChangePct; // Phần trăm thay đổi của tỷ lệ huỷ tour
}
