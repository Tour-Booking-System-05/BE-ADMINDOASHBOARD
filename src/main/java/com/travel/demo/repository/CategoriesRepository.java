package com.travel.demo.repository;

import com.travel.demo.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository  extends JpaRepository<Categories, Integer> {
    Page<Categories> findByDeletedAtIsNull(Pageable pageable);
    //Lấy bản ghi theo id mà trường deleted_at = null
    Categories findByCategoryIdAndDeletedAtIsNull(Integer id);
    Page<Categories> findByCategoryNameContainingIgnoreCaseAndDeletedAtIsNull(String categoryName, Pageable pageable);
    Page<Categories> findAll(Pageable pageable);
    List<Categories> findByDeletedAtIsNull();
    Optional<Categories> findByCategoryIdAndStatusAndDeletedAtIsNull(Integer categoryId, Boolean status);
    List<Categories> findByDeletedAtIsNullAndStatus(Boolean status);

}
