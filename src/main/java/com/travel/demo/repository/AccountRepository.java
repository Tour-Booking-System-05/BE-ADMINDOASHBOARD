package com.travel.demo.repository;

import com.travel.demo.entity.Accounts;
import com.travel.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountRepository extends JpaRepository<Accounts, Integer> {
    public Accounts findByEmail(String email);

    @Query("SELECT COUNT(a.accountId) FROM Accounts a " +
            "where a.role = 'USER' " +
            "and a.deleteAt is null " +
            "and a.createAt >= :start " +
            "and a.createAt <= :end")
    Long countUserBetween(@Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end);

    @Query(value = """
    SELECT
        DATE(a.created_at) AS created_at,
        COALESCE(COUNT(a.account_id), 0) AS total
    FROM accounts a
    WHERE a.created_at >= :from
      AND a.created_at <= :to
      AND a.role = 'USER'
    GROUP BY DATE(a.created_at)
    ORDER BY DATE(a.created_at)
    """, nativeQuery = true)
    List<Object[]> getCustomerLast7Days(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    Accounts findByEmailAndRole(String email, Role role);

    @Query("""
        select distinct a
        from Accounts a
        left join fetch a.roleEntity r
        left join fetch r.adminRolePermissions arp
        left join fetch arp.adminPermission p
        where a.email = :email
    """)
    Accounts findByEmailWithPermissions(@Param("email") String email);
    @Query("""
        select a
        from Accounts a
        join fetch a.roleEntity r
        where a.role = com.travel.demo.entity.Role.ADMIN
          and r.roleId = 1
          and a.email is not null
    """)
    List<Accounts> findAdminRoleId1();
}
