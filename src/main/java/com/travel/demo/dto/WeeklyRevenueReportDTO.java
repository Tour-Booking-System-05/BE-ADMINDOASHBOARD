package com.travel.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyRevenueReportDTO {
    private LocalDateTime dateTime;
    private Double revenue;
    private Integer totalOrders;
    private Integer totalCustomers;
}
