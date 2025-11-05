package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "roles")
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }
}
