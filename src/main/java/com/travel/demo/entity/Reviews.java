package com.travel.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reviews {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Integer reviewId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;

	private Double rating;
	private String comment;

	@Column(name = "image_urls", columnDefinition = "json")
	private String imageUrls;

	@Column(name = "rate_date")
	private LocalDateTime rateDate;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Items item;
}
