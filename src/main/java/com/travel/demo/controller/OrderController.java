package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.OrderDTO;
import com.travel.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderId,desc") String[] sort,
            @RequestParam(required = false) String keyword
    ){
        return ResponseEntity.ok(orderService.getAllOrders(page, size, sort, keyword));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    @PutMapping("/{id}")
    @ActivityAudit(
            type = "ORDER",
            entityType = "ORDER",
            entityIdParam = "id",
            statusParam = "orderDTO",   // tên biến trong method của bạn
            statusField = "status"      // field trong OrderDTO
    )
    public ResponseEntity<OrderDTO> update(@PathVariable Integer id, @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.update(id, orderDTO.getStatus()));
    }
    @GetMapping("/{id}/invoice")
    public ResponseEntity<OrderDTO> getInvoice(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.getInvoiceByOrderId(id));
    }
}
