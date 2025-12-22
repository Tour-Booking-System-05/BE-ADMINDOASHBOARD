package com.travel.demo.service.impl;

import com.travel.demo.dto.*;
import com.travel.demo.entity.OrderStatus;
import com.travel.demo.repository.OrderRepository;
import com.travel.demo.repository.UserRepository;
import com.travel.demo.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public StatisticDTO kpis(int range) {
//Xác định “kỳ hiện tại” và “kỳ trước”
        DateRange cur = resolveRange(range);
        DateRange prev = resolvePrev(cur);
//        2) Lấy số liệu KPI của kỳ hiện tại
        Long revenue = orderRepository.sumRevenue(cur.from, cur.to);
        Long orders  = orderRepository.countOrders(cur.from, cur.to);
        Long customers = userRepository.countNewCustomers(cur.from, cur.to);
//3) Tính tỷ lệ huỷ (cancelRate) kỳ hiện tại
        Long cancelled = orderRepository.countByStatus(cur.from, cur.to, (byte) 3);
        double cancelRate = (orders == null || orders == 0) ? 0d : (double) cancelled / (double) orders;
//4) Lấy số liệu KPI của kỳ trước (prev)
        Long prevRevenue = orderRepository.sumRevenue(prev.from, prev.to);
        Long prevOrders  = orderRepository.countOrders(prev.from, prev.to);
        Long prevCustomers = userRepository.countNewCustomers(prev.from, prev.to);
        Long prevCancelled = orderRepository.countByStatus(prev.from, prev.to, (byte) 3);
        double prevCancelRate = (prevOrders == null || prevOrders == 0) ? 0d : (double) prevCancelled / (double) prevOrders;
//        5) Trả về DTO KPI + các % thay đổi

        return new StatisticDTO(
                nvl(revenue), nvl(orders), nvl(customers), cancelRate,
                pctChange(nvl(prevRevenue), nvl(revenue)),
                pctChange(nvl(prevOrders), nvl(orders)),
                pctChange(nvl(prevCustomers), nvl(customers)),
                pctChange(prevCancelRate, cancelRate)
        );
    }
//Trả về chuỗi dữ liệu doanh thu theo tháng
    @Override
    public List<SeriesPointDTO> revenueSeries(int range) {
        DateRange cur = resolveRange(range);

        Map<YearMonth, Long> map = new HashMap<>();
        for (Object[] row : orderRepository.revenueByMonth(cur.from, cur.to)) {
            int y = ((Number) row[0]).intValue();
            int m = ((Number) row[1]).intValue();
            long v = ((Number) row[2]).longValue();
            map.put(YearMonth.of(y, m), v);
        }
        return fillMonths(cur, map);
    }

    @Override
    public List<OrderStatusReportDTO> orderStatus(int range) {
        DateRange cur = resolveRange(range);

        // đảm bảo đủ 3 trạng thái (nếu DB thiếu status nào đó vẫn trả về 0)
        Map<OrderStatus, Integer> countMap = new EnumMap<>(OrderStatus.class);
        for (OrderStatus s : OrderStatus.values()) countMap.put(s, 0);

        for (Object[] row : orderRepository.groupCountByStatus(cur.from, cur.to)) {
            Byte statusByte = (Byte) row[0];              
            int qty = ((Number) row[1]).intValue();

            OrderStatus status = fromByteStatus(statusByte); 
            countMap.put(status, qty);
        }

        List<OrderStatusReportDTO> out = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()) {
            out.add(new OrderStatusReportDTO(s, countMap.get(s)));
        }
        return out;
    }

    private OrderStatus fromByteStatus(Byte b) {
        if (b == null) return OrderStatus.PENDING;

        switch (b) {
            case 0: return OrderStatus.PENDING;   // đợi đi
            case 1: return OrderStatus.PROCESS;   // đang đi
            case 2: return OrderStatus.COMPLETE;  // đã đi
            case 3: return OrderStatus.CANCEL;    // hủy
            default: return OrderStatus.PENDING;
        }
    }



    @Override
    public CustomerVipDTO customerVip(int range) {
        Object[] r = userRepository.countVipLevels().get(0);
        return new CustomerVipDTO(
                ((Number) r[1]).longValue(), // BRONZE
                ((Number) r[2]).longValue(), // SILVER
                ((Number) r[3]).longValue(), // GOLD
                ((Number) r[4]).longValue()  // DIAMOND
        );    }

    @Override
    public List<SeriesPointDTO> customerSeries(int range) {
        DateRange cur = resolveRange(range);

        Map<YearMonth, Long> map = new HashMap<>();
        for (Object[] row : userRepository.customersByMonth(cur.from, cur.to)) {
            int y = ((Number) row[0]).intValue();
            int m = ((Number) row[1]).intValue();
            long v = ((Number) row[2]).longValue();
            map.put(YearMonth.of(y, m), v);
        }
        return fillMonths(cur, map);
    }

    @Override
    public DashboardDTO dashboard(int range) {
        return new DashboardDTO(
                kpis(range),
                revenueSeries(range),
                orderStatus(range),
                customerVip(range),
                customerSeries(range)
        );
    }

    @Override
    public DashboardDTO dailyReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime from = yesterday.atStartOfDay();
        LocalDateTime to = yesterday.plusDays(1).atStartOfDay();

        StatisticDTO dailyKpis = kpisCustom(from, to);
        List<OrderStatusReportDTO> dailyStatus = orderStatusCustom(from, to);

        return new DashboardDTO(
                dailyKpis,
                Collections.emptyList(),
                dailyStatus,
                customerVip(12),
                Collections.emptyList()
        );
    }

    private StatisticDTO kpisCustom(LocalDateTime from, LocalDateTime to) {
    Long revenue = orderRepository.sumRevenue(from, to);
    Long orders  = orderRepository.countOrders(from, to);
    Long customers = userRepository.countNewCustomers(from, to);
    Long cancelled = orderRepository.countByStatus(from, to, (byte) 3);
    double cancelRate = (orders == null || orders == 0) ? 0d : (double) cancelled / (double) orders;

    LocalDateTime prevFrom = from.minusDays(1);
    LocalDateTime prevTo   = to.minusDays(1);

    Long prevRevenue = orderRepository.sumRevenue(prevFrom, prevTo);
    Long prevOrders  = orderRepository.countOrders(prevFrom, prevTo);
    Long prevCustomers = userRepository.countNewCustomers(prevFrom, prevTo);
    Long prevCancelled = orderRepository.countByStatus(prevFrom, prevTo, (byte) 3);
    double prevCancelRate = (prevOrders == null || prevOrders == 0) ? 0d : (double) prevCancelled / (double) prevOrders;

    return new StatisticDTO(
            nvl(revenue), nvl(orders), nvl(customers), cancelRate,
            pctChange(nvl(prevRevenue), nvl(revenue)),
            pctChange(nvl(prevOrders), nvl(orders)),
            pctChange(nvl(prevCustomers), nvl(customers)),
            pctChange(prevCancelRate, cancelRate)
    );
}
    private List<OrderStatusReportDTO> orderStatusCustom(LocalDateTime from, LocalDateTime to) {
        Map<OrderStatus, Integer> countMap = new EnumMap<>(OrderStatus.class);
        for (OrderStatus s : OrderStatus.values()) countMap.put(s, 0);

        for (Object[] row : orderRepository.groupCountByStatus(from, to)) {
            OrderStatus status = (OrderStatus) row[0];
            int qty = ((Number) row[1]).intValue();
            countMap.put(status, qty);
        }

        List<OrderStatusReportDTO> out = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()) {
            out.add(new OrderStatusReportDTO(s, countMap.get(s)));
        }
        return out;
    }

    private DateRange resolveRange(int months) {
        if (!(months == 1 || months == 3 || months == 6 || months == 12)) {
            throw new IllegalArgumentException("range must be 1, 3, 6, 12");
        }
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMonths(months);
        return new DateRange(from, to, months);
    }

    private DateRange resolvePrev(DateRange cur) {
        LocalDateTime prevTo = cur.from;
        LocalDateTime prevFrom = prevTo.minusMonths(cur.months);
        return new DateRange(prevFrom, prevTo, cur.months);
    }

    private List<SeriesPointDTO> fillMonths(DateRange dr, Map<YearMonth, Long> values) {
        YearMonth end = YearMonth.from(dr.to);
        YearMonth start = end.minusMonths(dr.months - 1);

        List<SeriesPointDTO> out = new ArrayList<>();
        YearMonth cursor = start;
        while (!cursor.isAfter(end)) {
            long v = values.getOrDefault(cursor, 0L);
            out.add(new SeriesPointDTO("T" + cursor.getMonthValue(), v));
            cursor = cursor.plusMonths(1);
        }
        return out;
    }

    private long nvl(Long v) { return v == null ? 0L : v; }

    private Double pctChange(long prev, long curr) {
        if (prev == 0) return curr == 0 ? 0d : 100d;
        return ((double) (curr - prev) / (double) prev) * 100d;
    }
    private Double pctChange(double prev, double curr) {
        if (prev == 0) return curr == 0 ? 0d : 100d;
        return ((curr - prev) / prev) * 100d;
    }

    private static class DateRange {
        final LocalDateTime from;
        final LocalDateTime to;
        final int months;
        DateRange(LocalDateTime from, LocalDateTime to, int months) {
            this.from = from; this.to = to; this.months = months;
        }
    }
}
