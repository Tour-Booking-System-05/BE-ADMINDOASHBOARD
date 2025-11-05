package com.travel.demo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TourDTO {
        private Integer itemId;
        @NotNull(message = "Danh mục không được để trống")
        private Integer categoryId;
        @NotNull(message = "Hướng dẫn viên không được để trống")
        private Integer guiderId;
        @NotBlank(message = "Tên tour không được để trống")
        @Size(max = 255, message = "Tên tour vượt quá 255 ký tự")
        private String titleTour;
        private String description;
        private LocalDate dateTour;
        private LocalDate dateEndTour;
        private String located;
        private String vehicle;
        @Positive(message = "Giá so sánh phải lớn hơn giá tour")
        private BigDecimal comparatingPrice;
        private Float discount;
        @NotNull @Positive(message = "Giá tour phải lớn hơn 0")
        private BigDecimal price;
        @Min(value = 1, message = "Số vé phải ít nhất là 1")
        private Integer total;
        private Byte status;
        @NotEmpty(message = "Cần ít nhất 1 ảnh")
        private List<String> imageUrls;

    public TourDTO(Integer itemId, Integer categoryId, Integer guiderId, String titleTour, String description, LocalDate dateTour, LocalDate dateEndTour, String located, String vehicle, BigDecimal comparatingPrice, Float discount, BigDecimal price, Integer total, Byte status, List<String> imageUrls) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.guiderId = guiderId;
        this.titleTour = titleTour;
        this.description = description;
        this.dateTour = dateTour;
        this.dateEndTour = dateEndTour;
        this.located = located;
        this.vehicle = vehicle;
        this.comparatingPrice = comparatingPrice;
        this.discount = discount;
        this.price = price;
        this.total = total;
        this.status = status;
        this.imageUrls = imageUrls;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getGuiderId() {
        return guiderId;
    }

    public void setGuiderId(Integer guiderId) {
        this.guiderId = guiderId;
    }

    public String getTitleTour() {
        return titleTour;
    }

    public void setTitleTour(String titleTour) {
        this.titleTour = titleTour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateTour() {
        return dateTour;
    }

    public void setDateTour(LocalDate dateTour) {
        this.dateTour = dateTour;
    }

    public LocalDate getDateEndTour() {
        return dateEndTour;
    }

    public void setDateEndTour(LocalDate dateEndTour) {
        this.dateEndTour = dateEndTour;
    }

    public String getLocated() {
        return located;
    }

    public void setLocated(String located) {
        this.located = located;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public BigDecimal getComparatingPrice() {
        return comparatingPrice;
    }

    public void setComparatingPrice(BigDecimal comparatingPrice) {
        this.comparatingPrice = comparatingPrice;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
