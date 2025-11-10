package com.travel.demo.repository;

import com.travel.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    Page<Orders> findByItem_TitleTourContainingIgnoreCase(String keyword, Pageable pageable);

}
