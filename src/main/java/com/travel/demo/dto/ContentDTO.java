package com.travel.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
public class ContentDTO {
    private Integer contentId;
    @NotBlank(message = "Tiêu đề  không được để trống")
    @Size(max = 255, message = "Tiêu đề  vượt quá 255 ký tự")
    private String title;
    private String content;
    private Integer employeeId;
    private LocalDateTime createAt;
    private LocalDateTime publishedAt;
    private Byte status;
    private String imageUrl;

    public ContentDTO() {
    }

    public ContentDTO(Integer contentId, String title, String content, Integer employeeId, LocalDateTime createAt, LocalDateTime publishedAt, Byte status, String imageUrl) {
        this.contentId = contentId;
        this.title = title;
        this.content = content;
        this.employeeId = employeeId;
        this.createAt = createAt;
        this.publishedAt = publishedAt;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
