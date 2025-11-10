package com.travel.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {
    private Integer orderId;
    private String tourName;
    private String customerName;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private Byte status;
    private Integer amountTicket;

    public OrderDTO(Integer orderId, String tourName, String customerName, BigDecimal totalPrice, LocalDateTime orderDate, Byte status, Integer amountTicket) {
        this.orderId = orderId;
        this.tourName = tourName;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
        this.amountTicket = amountTicket;
    }

    public OrderDTO() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getAmountTicket() {
        return amountTicket;
    }

    public void setAmountTicket(Integer amountTicket) {
        this.amountTicket = amountTicket;
    }
}
