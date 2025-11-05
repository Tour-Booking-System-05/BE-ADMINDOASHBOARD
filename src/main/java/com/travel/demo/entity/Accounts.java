package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")

public class Accounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;
	@Column(name = "is_admin_root")
	private Boolean isAdminRoot = false;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createAt;

	@Column(name = "deleted_at")
	private LocalDateTime deleteAt;

	@Column(name = "last_activity")
	private LocalDateTime lastActivity;

	private Integer point = 0;

	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Column(name = "image_url")
	private String imageUrl;
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Carts cart;

	public Carts getCart() {
		return cart;
	}

	public void setCart(Carts cart) {
		this.cart = cart;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", referencedColumnName = "role_id")
	private Roles roleEntity;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAdminRoot() {
		return isAdminRoot;
	}

	public void setAdminRoot(Boolean adminRoot) {
		isAdminRoot = adminRoot;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

	public LocalDateTime getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(LocalDateTime deleteAt) {
		this.deleteAt = deleteAt;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Roles getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(Roles roleEntity) {
		this.roleEntity = roleEntity;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
