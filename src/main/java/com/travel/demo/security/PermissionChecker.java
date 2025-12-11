package com.travel.demo.security;

import com.travel.demo.entity.Accounts;
import com.travel.demo.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionChecker")

public class PermissionChecker {
    public boolean hasPermission(Authentication authentication, String permissionName) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Accounts)) {
            return false;
        }
//        Trong Spring Security, principal là đối tượng đại diện cho người dùng sau khi đã đăng nhập thành công.
        Accounts accounts = (Accounts) authentication.getPrincipal();
        // 1. Chỉ cho phép account có role enum = ADMIN vào khu admin
        if (accounts.getRole() != Role.ADMIN) {
            return false;
        }
        // 2. Phải có roleEntity (mapping tới bảng roles)
        if (accounts.getRoleEntity() == null || accounts.getRoleEntity().getAdminRolePermissions() == null) {
            return false;
        }
        // 3. Kiểm tra permission trong role đó
        return accounts.getRoleEntity()
                .getAdminRolePermissions()
                .stream()
                .anyMatch(arp -> arp.getAdminPermission().getName().equals(permissionName));
    }
    }



