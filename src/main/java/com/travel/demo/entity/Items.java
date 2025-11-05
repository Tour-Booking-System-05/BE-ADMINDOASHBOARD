package com.travel.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
public class Items {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Integer itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Categories category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employees guider;

	@Column(name = "title_tour")
	private String titleTour;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;


	@Column(name = "date_tour")
	private LocalDate dateTour;

	@Column(name = "date_end_tour")
	private LocalDate dateEndTour;

	private String located;
	private String vehicle;

	@Column(name = "comparating_price", precision = 10, scale = 2)
	private BigDecimal comparatingPrice;

	private Float discount;

	@Column(precision = 10, scale = 2)
	private BigDecimal price;

	private Integer total;
	@Column(name = "status")
	private Byte status;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDate deletedAt;

	@Column(name = "image_urls", columnDefinition = "json")
	private String imageUrls;

	public Items() {
	}

	public Items(Integer itemId, Categories category, Employees guider, String titleTour, String description, LocalDate dateTour, LocalDate dateEndTour, String located, String vehicle, BigDecimal comparatingPrice, Float discount, BigDecimal price, Integer total, Byte status, LocalDateTime createdAt, LocalDate deletedAt, String imageUrls) {
		this.itemId = itemId;
		this.category = category;
		this.guider = guider;
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
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.imageUrls = imageUrls;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Categories getCategory() {
		return category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}

	public Employees getGuider() {
		return guider;
	}

	public void setGuider(Employees guider) {
		this.guider = guider;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDate deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}
}
