package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String fullname;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "phone_number", length = 15)
	private String phoneNumber;

	// ⚙️ Một user thuộc về một account
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Accounts account;

	// ⚙️ Một user có thể có nhiều review
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Reviews> reviews = new HashSet<>();

	// ⚙️ Nếu bạn có bảng user_promotions (ManyToMany với Promotion)
	@ManyToMany
	@JoinTable(
			name = "user_promotions",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "promotion_id")
	)
	private Set<Promotions> promotions = new HashSet<>();
}
