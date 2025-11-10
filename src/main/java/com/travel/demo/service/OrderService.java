package com.travel.demo.service;

import com.travel.demo.dto.OrderDTO;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderDTO> getAllOrders(int page, int size, String[] sort, String keyword);

    OrderDTO getOrderById(Integer id);

    OrderDTO update(Integer id, Byte status);

    OrderDTO getInvoiceByOrderId(Integer id);
}
