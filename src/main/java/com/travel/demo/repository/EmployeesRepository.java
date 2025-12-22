package com.travel.demo.repository;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
    @Query("SELECT e FROM Employees e WHERE e.account.deleteAt IS NULL")
    List<Employees> findByDeletedAtIsNull();
    Optional<Employees> findById(Integer id);
    @Query("SELECT e FROM Employees e JOIN Users u ON u.account.accountId = e.account.accountId WHERE u.username = :username")
    Employees findByUserUsername(String username);
    Optional<Employees> findByAccountEmail(String email);
    Optional<Employees> findByAccount_AccountId(Integer accountId);
    Page<Employees> findByAccount_DeleteAtIsNull(Pageable pageable);
    Optional<Employees> findByEmployeeIdAndAccount_DeleteAtIsNull(Integer employeeId);

    // Lọc thêm theo tên (search)
    Page<Employees> findByAccount_DeleteAtIsNullAndFullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

}
