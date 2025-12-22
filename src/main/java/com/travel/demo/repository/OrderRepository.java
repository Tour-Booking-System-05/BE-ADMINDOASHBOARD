package com.travel.demo.repository;

import com.travel.demo.dto.WeeklyRevenueReportDTO;
import com.travel.demo.entity.OrderStatus;
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
    Page<Orders> findByItem_Guider_EmployeeIdAndDeleteAtIsNull(Integer guiderId, Pageable pageable);

    @Query("""
    select coalesce(sum(o.item.price * o.amountTicket), 0)
    from Orders o
    where o.date >= :from
      and o.date <= :to
      and o.deleteAt is null
""")
    Long totalRevenue(@Param("from") LocalDateTime from,
                           @Param("to") LocalDateTime to);

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

    @Query("""
    select coalesce(sum(o.item.price * o.amountTicket), 0)
    from Orders o
    where o.date >= :from
      and o.date < :to
      and o.deleteAt is null
""")
    Long sumRevenue(@Param("from") LocalDateTime from,
                    @Param("to") LocalDateTime to);

    @Query("""
    select count(o.orderId)
    from Orders o
    where o.date >= :from
      and o.date < :to
      and o.deleteAt is null
""")
    Long countOrders(@Param("from") LocalDateTime from,
                     @Param("to") LocalDateTime to);

    @Query("""
    select count(o.orderId)
    from Orders o
    where o.date >= :from
      and o.date < :to
      and o.status = :status
      and o.deleteAt is null
""")
    Long countByStatus(@Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to,
                       @Param("status") Byte status);


    // trả về: status, count
    @Query("""
    select o.status, count(o.orderId)
    from Orders o
    where o.date >= :from
      and o.date < :to
      and o.deleteAt is null
    group by o.status
""")
    List<Object[]> groupCountByStatus(@Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to);


    // MySQL: year/month
    // trả về: y, m, revenue
    @Query(value = """
    select
        year(o.date) as y,
        month(o.date) as m,
        coalesce(sum(i.price * o.amount_ticket), 0) as revenue
    from orders o
    join items i on o.item_id = i.item_id
    where o.date >= :from
      and o.date < :to
      and o.deleted_at is null
    group by year(o.date), month(o.date)
    order by year(o.date), month(o.date)
""", nativeQuery = true)
    List<Object[]> revenueByMonth(@Param("from") LocalDateTime from,
                                  @Param("to") LocalDateTime to);



}
