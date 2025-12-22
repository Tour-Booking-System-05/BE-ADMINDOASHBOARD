package com.travel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DashboardDTO {
    private StatisticDTO kpis;
    private List<SeriesPointDTO> revenueSeries;
    private List<OrderStatusReportDTO> orderStatus;
    private CustomerVipDTO customerVip;
    private List<SeriesPointDTO> customerSeries;
}
