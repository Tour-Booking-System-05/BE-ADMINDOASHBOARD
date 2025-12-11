package com.travel.demo.service.impl;

import com.travel.demo.dto.BaseOverViewReportDTO;
import com.travel.demo.dto.OrderStatusReportDTO;
import com.travel.demo.dto.WeeklyRevenueReportDTO;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.OrderRepository;
import com.travel.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    private static final double EPS = 1e-6;
    // Day
    LocalDate today = LocalDate.now();
    LocalDateTime startToday = today.atStartOfDay();
    LocalDateTime startTomorrow = today.plusDays(1).atStartOfDay();
    LocalDateTime startYesterday = today.minusDays(1).atStartOfDay();

    // Month
    YearMonth currentMonth = YearMonth.now();
    YearMonth lastMonth = currentMonth.minusMonths(1);
    YearMonth nextMonth = currentMonth.plusMonths(1);
    LocalDateTime startThisMonth = currentMonth.atDay(1).atStartOfDay();
    LocalDateTime startNextMonth = nextMonth.atDay(1).atStartOfDay();
    LocalDateTime startLastMonth = lastMonth.atDay(1).atStartOfDay();

    // Year
    int currentYear = Year.now().getValue();
    LocalDateTime startThisYear = LocalDate.of(currentYear, 1, 1).atStartOfDay();
    LocalDateTime startLastYear  = startThisYear.minusYears(1);
    LocalDateTime startNextYear  = startThisYear.plusYears(1);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public BaseOverViewReportDTO overviewRevenue(String filter) {



        try {
            BaseOverViewReportDTO revenue = new BaseOverViewReportDTO();

            if (filter.equals("Month")) {
                double thisMonthTotal = orderRepository.totalRevenue(startThisMonth, startNextMonth);
                double lastMonthTotal = orderRepository.totalRevenue(startLastMonth, startThisMonth);
                revenue.setValue(BigDecimal.valueOf(thisMonthTotal).setScale(0, RoundingMode.HALF_UP));
                revenue.setPercent(calculateGrowth(thisMonthTotal, lastMonthTotal));
            }
            if (filter.equals("Year")) {
                double todayTotal = orderRepository.totalRevenue(startThisYear, startNextYear);
                double yesterdayTotal = orderRepository.totalRevenue(startLastYear, startThisYear);
                revenue.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                revenue.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            else {
                double todayTotal = orderRepository.totalRevenue(startToday, startTomorrow);
                double yesterdayTotal = orderRepository.totalRevenue(startYesterday, startToday);
                revenue.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                revenue.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            return revenue;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private double calculateGrowth(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((double) (current - previous) / previous) * 100;
    }
    private double calculateGrowth(double current, double previous) {
        if (Math.abs(previous) < EPS) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((double) (current - previous) / previous) * 100;
    }




    @Override
    public BaseOverViewReportDTO overviewOrders(String filter) {

        try {
            BaseOverViewReportDTO orderTotals = new BaseOverViewReportDTO();

            if (filter.equals("Month")) {
                double thisMonthTotal = orderRepository.countOrder(startThisMonth, startNextMonth);
                double lastMonthTotal = orderRepository.countOrder(startLastMonth, startThisMonth);
                orderTotals.setValue(BigDecimal.valueOf(thisMonthTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(thisMonthTotal, lastMonthTotal));
            }
            if (filter.equals("Year")) {
                double todayTotal = orderRepository.countOrder(startThisYear, startNextYear);
                double yesterdayTotal = orderRepository.countOrder(startLastYear, startThisYear);
                orderTotals.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            else {
                double todayTotal = orderRepository.countOrder(startToday, startTomorrow);
                double yesterdayTotal = orderRepository.countOrder(startYesterday, startToday);
                orderTotals.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            return orderTotals;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseOverViewReportDTO overviewCustomer(String filter) {
        try {
            BaseOverViewReportDTO orderTotals = new BaseOverViewReportDTO();

            if (filter.equals("Month")) {
                double thisMonthTotal = accountRepository.countUserBetween(startThisMonth, startNextMonth);
                double lastMonthTotal = accountRepository.countUserBetween(startLastMonth, startThisMonth);
                orderTotals.setValue(BigDecimal.valueOf(thisMonthTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(thisMonthTotal, lastMonthTotal));
            }
            if (filter.equals("Year")) {
                double todayTotal = accountRepository.countUserBetween(startThisYear, startNextYear);
                double yesterdayTotal = accountRepository.countUserBetween(startLastYear, startThisYear);
                orderTotals.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            else {
                double todayTotal = accountRepository.countUserBetween(startToday, startTomorrow);
                double yesterdayTotal = accountRepository.countUserBetween(startYesterday, startToday);
                orderTotals.setValue(BigDecimal.valueOf(todayTotal).setScale(0, RoundingMode.HALF_UP));
                orderTotals.setPercent(calculateGrowth(todayTotal, yesterdayTotal));
            }
            return orderTotals;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WeeklyRevenueReportDTO> weeklyRevenue() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();
        List<Object[]> rows = orderRepository.getRevenueLast7Days(from, to);

        // 2. Map thành Map<LocalDate, Double> để tra cho tiện
        Map<LocalDate, WeeklyRevenueReportDTO> revenueByDate = new HashMap<>();
        for (Object[] row : rows) {
            LocalDate day;

            Object dateObj = row[0];
            if (dateObj instanceof Date d) { // java.sql.Date
                day = d.toLocalDate();
            } else if (dateObj instanceof Timestamp ts) {
                day = ts.toLocalDateTime().toLocalDate();
            } else {
                day = LocalDate.parse(dateObj.toString());
            }

            Double revenue = ((BigDecimal) row[1]).doubleValue();
            Integer totalOrders =  ((Number) row[2]).intValue();
            revenueByDate.put(day, new WeeklyRevenueReportDTO(day.atStartOfDay(), revenue, totalOrders, 0 ));
        }

        List<Object[]> rawsAccount = accountRepository.getCustomerLast7Days(from, to);

        for (Object[] row : rawsAccount) {
            LocalDate day;

            Object dateObj = row[0];
            if (dateObj instanceof Date d) { // java.sql.Date
                day = d.toLocalDate();
            } else if (dateObj instanceof Timestamp ts) {
                day = ts.toLocalDateTime().toLocalDate();
            } else {
                day = LocalDate.parse(dateObj.toString());
            }

            Integer totalCustomers =  ((Number) row[1]).intValue();

            if (revenueByDate.containsKey(day)) {
                revenueByDate.get(day).setTotalCustomers(totalCustomers);
            } else {
                revenueByDate.put(day, new WeeklyRevenueReportDTO(day.atStartOfDay(), 0.0, 0, totalCustomers ));
            }

        }

        // 3. Bù đủ 7 ngày: ngày nào không có trong map thì revenue = 0
        List<WeeklyRevenueReportDTO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate d = startDate.plusDays(i);
            WeeklyRevenueReportDTO dto = revenueByDate.getOrDefault(d, new WeeklyRevenueReportDTO());
            if (dto.getDateTime() == null) {
                dto.setDateTime(d.atStartOfDay());
            }
            if (dto.getRevenue() == null) {
                dto.setRevenue(0.0);
            }
            if (dto.getTotalOrders() == null) {
                dto.setTotalOrders(0);
            }
            dto.setTotalCustomers(0);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<OrderStatusReportDTO> orderStatus() {
        return List.of();
    }
}
