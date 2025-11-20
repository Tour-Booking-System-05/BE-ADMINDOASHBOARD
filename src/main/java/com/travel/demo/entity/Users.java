package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(nullable = false, unique = true, length = 45)
	private String username;

	@Column(nullable = false, length = 255)
	private String fullname;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	// user_rank ENUM('BRONZE','SILVER','GOLD','DIAMOND')
	@Enumerated(EnumType.STRING)
	@Column(name = "user_rank", nullable = false)
	private UserRank userRank;

	// Many users → one account
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Accounts account;
	// MANY TO MANY WITH PROMOTIONS
	@ManyToMany
	@JoinTable(
			name = "user_promotions",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "promotion_id")
	)
	private Set<Promotions> promotions = new HashSet<>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UserRank getUserRank() {
		return userRank;
	}

	public void setUserRank(UserRank userRank) {
		this.userRank = userRank;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public Users(Integer userId, String username, String fullname, LocalDate dateOfBirth, String phoneNumber, UserRank userRank, Accounts account) {
		this.userId = userId;
		this.username = username;
		this.fullname = fullname;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.userRank = userRank;
		this.account = account;
	}

	public Users() {
	}
}
