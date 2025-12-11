package com.travel.demo.repository;

import com.travel.demo.entity.AdminRolePermissions;
import com.travel.demo.entity.AdminRolePermissionsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRolePermissionsRepository extends JpaRepository<AdminRolePermissions, AdminRolePermissionsId> {
    List<AdminRolePermissions> findByRoleRoleId(Integer RoleId);
}
