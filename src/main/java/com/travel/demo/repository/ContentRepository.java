package com.travel.demo.repository;

import com.travel.demo.entity.Categories;
import com.travel.demo.entity.Contents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Contents, Integer> {
    Page<Contents> findByDeletedAtIsNull(Pageable pageable);
    Page<Contents> findByTitleContainingIgnoreCaseAndDeletedAtIsNull(String keyword, Pageable pageable);
    Optional<Contents> findByContentIdAndStatusAndDeletedAtIsNull(Integer contentId, Byte status);
    Contents findByContentIdAndDeletedAtIsNull(Integer id);

}
