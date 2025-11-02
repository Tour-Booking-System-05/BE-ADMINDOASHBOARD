package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Integer itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cate_id")
	private Categories category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guider_id")
	private Employees guider;

	@Column(name = "title_tour")
	private String titleTour;

	@Column(columnDefinition = "TEXT")
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
	private Boolean status;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDate deletedAt;

	@Column(name = "image_urls", columnDefinition = "json")
	private String imageUrls;
}
