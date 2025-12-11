package com.travel.demo.repository;

import com.travel.demo.entity.AdminPermissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPermissionsRepository extends JpaRepository<AdminPermissions, Integer> {
}
