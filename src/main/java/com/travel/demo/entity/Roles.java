package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name", nullable = false)
    private String name;

    // Quan hệ 1-n với bảng trung gian
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminRolePermissions> adminRolePermissions;

    // Quan hệ ngược tới Accounts (1-n)
    @OneToMany(mappedBy = "roleEntity")
    private List<Accounts> accounts;
}
