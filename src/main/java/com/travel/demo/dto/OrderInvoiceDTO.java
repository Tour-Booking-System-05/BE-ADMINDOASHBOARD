package com.travel.demo.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class OrderInvoiceDTO {
    private String invoiceNo;
    private String customerName;
    private String customerPhone;
    private String tourName;
    private String tourLocation;
    private Date startDate;
    private Date endDate;
    private int amountTicket;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String paymentMethod;
    private String transactionCode;
    private int status;

    public OrderInvoiceDTO(String invoiceNo, String customerName, String customerPhone, String tourName, String tourLocation, Date startDate, Date endDate, int amountTicket, BigDecimal totalPrice, LocalDateTime orderDate, String paymentMethod, String transactionCode, int status) {
        this.invoiceNo = invoiceNo;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.tourName = tourName;
        this.tourLocation = tourLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountTicket = amountTicket;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.transactionCode = transactionCode;
        this.status = status;
    }

    public OrderInvoiceDTO() {
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourLocation() {
        return tourLocation;
    }

    public void setTourLocation(String tourLocation) {
        this.tourLocation = tourLocation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmountTicket() {
        return amountTicket;
    }

    public void setAmountTicket(int amountTicket) {
        this.amountTicket = amountTicket;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}