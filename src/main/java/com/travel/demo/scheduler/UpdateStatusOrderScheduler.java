package com.travel.demo.scheduler;

import com.travel.demo.entity.Items;
import com.travel.demo.entity.Orders;
import com.travel.demo.repository.ItemRepository;
import com.travel.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class UpdateStatusOrderScheduler {

    @Autowired
    private OrderRepository orderRepository;
    @Transactional
    @Scheduled(cron = "0 0/15 * * * ?", zone = "Asia/Ho_Chi_Minh")
    public void updateStatus() {

        List<Orders> list = orderRepository.findAll();
        for (Orders order : list) {
            // Bỏ qua đơn đã hủy
            if (order.getStatus() == 3) continue;

            // Nếu user hoặc tour bị xóa → set hủy
            if (order.getUser().getAccount().getDeleteAt() != null
                    || order.getItem().getDeletedAt() != null) {
                order.setStatus((byte) 3);
                continue;
            }

            // Mapping trạng thái tour -> đơn hàng
            switch (order.getItem().getStatus()) {
                case 1: // Tour đang hoạt động -> đơn đợi đi
                    order.setStatus((byte) 0);
                    break;
                case 2: // Tour đang đi -> đơn đang đi
                    order.setStatus((byte) 1);
                    break;
                case 0: // Tour đã kết thúc -> đơn đã đi
                    order.setStatus((byte) 2);
                    break;
                default:
                    break;
            }
        }
    }

    }

