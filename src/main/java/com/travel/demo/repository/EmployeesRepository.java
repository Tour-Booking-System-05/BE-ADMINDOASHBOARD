package com.travel.demo.repository;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
    @Query("SELECT e FROM Employees e WHERE e.account.deleteAt IS NULL")
    List<Employees> findByDeletedAtIsNull();
}
