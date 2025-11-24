package com.travel.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PromotionDTO {
    private Integer promotionId;
    @NotBlank(message = "Tên mã khuyẽn mãi không được để trống")
    @Size(max = 255, message = "Tên mã khuyến mãi không được vượt quá 255 ký tự")
    private String title;
    @NotBlank(message = "Code mã khuyẽn mãi không được để trống")
    @Size(max = 50, message = "Code mã khuyến mãi không được vượt quá 255 ký tự")
    private String code;
    private Double percentDecrease;
    private Integer categoryId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public PromotionDTO(Integer promotionId, String title, String code, Double percentDecrease, Integer categoryId, LocalDate startDate, LocalDate endDate, String description) {
        this.promotionId = promotionId;
        this.title = title;
        this.code = code;
        this.percentDecrease = percentDecrease;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public PromotionDTO() {
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPercentDecrease() {
        return percentDecrease;
    }

    public void setPercentDecrease(Double percentDecrease) {
        this.percentDecrease = percentDecrease;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
