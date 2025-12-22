package com.travel.demo.repository;

import com.travel.demo.entity.AccountStatus;
import com.travel.demo.entity.Role;
import com.travel.demo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer> {

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
    @Query("""
    select count(distinct o.user.userId)
    from Orders o
    where o.date >= :from
      and o.date < :to
      and o.deleteAt is null
""")
    Long countNewCustomers(@Param("from") LocalDateTime from,
                           @Param("to") LocalDateTime to);


    // trả về: y, m, total
    @Query("""
    select year(a.createAt), month(a.createAt), count(u)
    from Users u
    join u.account a
    where a.role = com.travel.demo.entity.Role.USER
      and a.createAt >= :from
      and a.createAt < :to
    group by year(a.createAt), month(a.createAt)
    order by year(a.createAt), month(a.createAt)
""")
    List<Object[]> customersByMonth(@Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);


    // NORMAL, BRONZE, SILVER, GOLD, DIAMOND (JPQL)
    @Query("""
    select
      0,
      sum(case when u.userRank = com.travel.demo.entity.UserRank.BRONZE then 1 else 0 end),
      sum(case when u.userRank = com.travel.demo.entity.UserRank.SILVER then 1 else 0 end),
      sum(case when u.userRank = com.travel.demo.entity.UserRank.GOLD then 1 else 0 end),
      sum(case when u.userRank = com.travel.demo.entity.UserRank.DIAMOND then 1 else 0 end)
    from Users u
""")
    List<Object[]> countVipLevels();



}

