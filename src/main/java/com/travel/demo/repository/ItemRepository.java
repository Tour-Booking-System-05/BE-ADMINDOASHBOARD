package com.travel.demo.repository;

import com.travel.demo.entity.Categories;
import com.travel.demo.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Items, Integer> {
    Page<Items> findByDeletedAtIsNull(Pageable pageable);
    Items findByItemIdAndDeletedAtIsNull(Integer id);
    Page<Items> findByTitleTourContainingIgnoreCaseAndDeletedAtIsNull(String itemName, Pageable pageable);
    Page<Items> findAll(Pageable pageable);
    List<Items> findByDeletedAtIsNull();
    Page<Items> findByGuider_EmployeeIdAndDeletedAtIsNull(Integer guiderId, Pageable pageable);

}

