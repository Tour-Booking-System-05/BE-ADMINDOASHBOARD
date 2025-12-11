package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "admin_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPermissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	// Quan hệ 1-n với bảng trung gian tự động cascade sang PermissionDetail.
	@OneToMany(mappedBy = "adminPermission", cascade = CascadeType.ALL)
	private List<AdminRolePermissions> adminRolePermissions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AdminRolePermissions> getAdminRolePermissions() {
		return adminRolePermissions;
	}

	public void setAdminRolePermissions(List<AdminRolePermissions> adminRolePermissions) {
		this.adminRolePermissions = adminRolePermissions;
	}
}
