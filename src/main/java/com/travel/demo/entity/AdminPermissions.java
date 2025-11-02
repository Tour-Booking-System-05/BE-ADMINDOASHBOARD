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

	@Column(name = "name")
	private String name;

	// Quan hệ 1-n với bảng trung gian
	@OneToMany(mappedBy = "adminPermission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdminRolePermissions> adminRolePermissions;
}
