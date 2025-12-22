package com.travel.demo.controller;

import com.travel.demo.dto.*;
import com.travel.demo.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistic")
@CrossOrigin(origins = "*")
public class StatisticsController {
    @Autowired
    private StatisticService statisticService;
    @GetMapping("/kpis")
    public StatisticDTO kpis(@RequestParam int range) {
        return statisticService.kpis(range);
    }

    @GetMapping("/revenue-series")
    public List<SeriesPointDTO> revenueSeries(@RequestParam int range) {
        return statisticService.revenueSeries(range);
    }

    @GetMapping("/order-status")
    public List<OrderStatusReportDTO> orderStatus(@RequestParam int range) {
        return statisticService.orderStatus(range);
    }

    @GetMapping("/customer-vip")
    public CustomerVipDTO customerVip(@RequestParam int range) {
        return statisticService.customerVip(range);
    }

    @GetMapping("/customer-series")
    public List<SeriesPointDTO> customerSeries(@RequestParam int range) {
        return statisticService.customerSeries(range);
    }

    // optional: load 1 phát
    @GetMapping("/dashboard")
    public DashboardDTO dashboard(@RequestParam int range) {
        return statisticService.dashboard(range);
    }
}
