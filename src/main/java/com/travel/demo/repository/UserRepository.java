package com.travel.demo.repository;

import com.travel.demo.entity.AccountStatus;
import com.travel.demo.entity.Role;
import com.travel.demo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

    // GET ALL USERS với các điều kiện về account
    Page<Users> findByAccount_DeleteAtIsNullAndAccount_StatusAndAccount_RoleAndAccount_IsAdminRoot(
            AccountStatus status,
            Role role,
            boolean isAdminRoot,
            Pageable pageable
    );

    // GET BY ID
    Users findByUserIdAndAccount_DeleteAtIsNullAndAccount_StatusAndAccount_RoleAndAccount_IsAdminRoot(
            Integer id,
            AccountStatus status,
            Role role,
            boolean isAdminRoot
    );

    // SEARCH FULLNAME
    Page<Users> findByFullnameContainingIgnoreCaseAndAccount_DeleteAtIsNullAndAccount_StatusAndAccount_RoleAndAccount_IsAdminRoot(
            String fullname,
            AccountStatus status,
            Role role,
            boolean isAdminRoot,
            Pageable pageable
    );

    // Lấy tất cả user (ACTIVE + INACTIVE), chỉ lọc deleteAt và role USER
    Page<Users> findByAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
            Role role,
            boolean isAdminRoot,
            Pageable pageable
    );

    // Lấy user theo ID (ACTIVE + INACTIVE)
    Users findByUserIdAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
            Integer id,
            Role role,
            boolean isAdminRoot
    );

    // Tìm kiếm theo fullname (ACTIVE + INACTIVE)
    Page<Users> findByFullnameContainingIgnoreCaseAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
            String fullname,
            Role role,
            boolean isAdminRoot,
            Pageable pageable
    );
}

