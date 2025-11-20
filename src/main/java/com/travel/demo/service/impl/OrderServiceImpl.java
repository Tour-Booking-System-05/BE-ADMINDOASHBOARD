package com.travel.demo.service.impl;

import com.travel.demo.dto.OrderDTO;
import com.travel.demo.entity.Orders;
import com.travel.demo.entity.Users;
import com.travel.demo.repository.OrderRepository;
import com.travel.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Page<OrderDTO> getAllOrders(int page, int size, String[] sort, String keyword) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Orders> orders;
        if(keyword != null && !keyword.trim().isEmpty()){
            orders = orderRepository.findByItem_TitleTourContainingIgnoreCase(keyword.trim(),pageable);
        }
        else{
            orders = orderRepository.findAll(pageable);
        }

        LocalDate today = LocalDate.now();
        List<OrderDTO> getListOrder = orders.stream().map(
                order->{
                    BigDecimal totalPrice = order.getItem().getPrice()
                            .multiply(BigDecimal.valueOf(order.getAmountTicket()));
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(order.getOrderId());
                    orderDTO.setTourName(order.getItem().getTitleTour());
                    orderDTO.setTotalPrice(totalPrice);
                    orderDTO.setOrderDate(order.getDate());
                    orderDTO.setStatus(order.getStatus());
                    orderDTO.setCustomerName(
                            Optional.ofNullable(order.getUser())
                                    .map(Users::getFullname)
                                    .orElse("Ẩn danh")
                    );
                    return orderDTO;
                }).collect(Collectors.toList());
        return new PageImpl<>(getListOrder, pageable, orders.getTotalElements());
    }

    @Override
    public OrderDTO getOrderById(Integer id) {
//        .orElseThrow(exception) Tự ném exception có thông báo rõ ràng nếu không tìm thấy
        Orders orders = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy dữ liệu đơn hàng có id"+ id));
        BigDecimal totalPrice = orders.getItem().getPrice()
                .multiply(BigDecimal.valueOf(orders.getAmountTicket()));
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orders.getOrderId());
        orderDTO.setTourName(orders.getItem().getTitleTour());
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setOrderDate(orders.getDate());
        orderDTO.setStatus(orders.getStatus());
        orderDTO.setCustomerName(
                Optional.ofNullable(orders.getUser())
                        .map(Users::getFullname)
                        .orElse("Ẩn danh")
        );        orderDTO.setAmountTicket(orders.getAmountTicket());
        return orderDTO;
    }

    @Override
    public OrderDTO update(Integer id, Byte status) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order có id = " + id));

        // Nếu tour hiện tại có status 0 hoặc 2 thì không cho phép đổi sang trạng thái hủy
        if ((orders.getStatus() == 0 || orders.getStatus() == 2) && status == 1) {
            throw new RuntimeException("Tour đang ở trạng thái không cho phép hủy (0 hoặc 2).");
        }

        orders.setStatus(status);
        orderRepository.save(orders);

        return getOrderById(id);
    }


    @Override
    public OrderDTO getInvoiceByOrderId(Integer id) {
        return null;
    }
}
