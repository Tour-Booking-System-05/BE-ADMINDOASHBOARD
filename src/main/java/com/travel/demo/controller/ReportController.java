package com.travel.demo.controller;

import com.travel.demo.dto.ApiResponse;
import com.travel.demo.dto.BaseOverViewReportDTO;
import com.travel.demo.dto.OverviewReportDTO;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.service.AuthService;
import com.travel.demo.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/overview/orders")
    public ResponseEntity<?> overviewOrders(@RequestParam(name = "filter") String filter){
        BaseOverViewReportDTO list = reportService.overviewOrders(filter);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/overview/revenue")
    public ResponseEntity<?> overviewRevenue(@RequestParam(name = "filter") String filter){
        BaseOverViewReportDTO list = reportService.overviewRevenue(filter);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/overview/customer")
    public ResponseEntity<?> overviewCustomer(@RequestParam(name = "filter") String filter){
        BaseOverViewReportDTO list = reportService.overviewCustomer(filter);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/report-weekly")
    public ResponseEntity<?> reportWeekly() {
        return ResponseEntity.ok(reportService.weeklyRevenue());
    }

}
