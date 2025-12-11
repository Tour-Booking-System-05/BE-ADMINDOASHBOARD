package com.travel.demo.repository;

import com.travel.demo.dto.WeeklyRevenueReportDTO;
import com.travel.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    Page<Orders> findByItem_TitleTourContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("Select COALESCE(SUM(i.price * o.amountTicket), 0) " +
            "from Orders o join Items i on o.item.itemId = i.itemId " +
            "where o.status = 2 " +
            "and o.deleteAt is null " +
            "and o.date >= :start " +
            "and o.date <= :end ")
    Double totalRevenue(@Param(value = "start") LocalDateTime start,
                        @Param(value = "end") LocalDateTime end);

    @Query("Select COUNT(o.orderId) from Orders o " +
            "where o.status = 2 " +
            "and o.deleteAt is null " +
            "and o.date >= :start " +
            "and o.date <= :end ")
    Long countOrder(@Param(value = "start") LocalDateTime start,
                    @Param(value = "end") LocalDateTime end);

    @Query(value = """
    SELECT 
        DATE(o.date) AS date,
        COALESCE(SUM(i.price * o.amount_ticket), 0) AS revenue,
        COALESCE(COUNT(o.order_id), 0) AS total
    FROM orders o
    JOIN items i ON o.item_id = i.item_id
    WHERE o.date >= :from
      AND o.date <= :to
    GROUP BY DATE(o.date)
    ORDER BY DATE(o.date)
    """, nativeQuery = true)
    List<Object[]> getRevenueLast7Days(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
