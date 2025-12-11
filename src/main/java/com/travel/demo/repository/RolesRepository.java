package com.travel.demo.repository;

import com.travel.demo.entity.Role;
import com.travel.demo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
}
