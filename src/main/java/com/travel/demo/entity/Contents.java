package com.travel.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contents {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "content_id")
	private Integer contentId;

	private String title;
	private String content;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employees employee;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	@Column(name = "published_at")
	private LocalDateTime publishedAt;

	private Byte status;

	@Column(name = "delete_at")
	private LocalDateTime deleteAt;

	@Column(name = "image_url")
	private String imageUrl;
}
