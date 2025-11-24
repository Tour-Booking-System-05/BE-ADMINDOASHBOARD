package com.travel.demo.repository;

import com.travel.demo.entity.Promotions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotions, Integer> {
    Page<Promotions> findByDeletedAtIsNull(Pageable pageable);
    Page<Promotions> findByDeletedAtIsNullAndTitleContainingIgnoreCaseOrDeletedAtIsNullAndCodeContainingIgnoreCase(
            String titleKeyword,
            String codeKeyword,
            Pageable pageable
    );

    List<Promotions> findByPromotionIdIn(List<Integer> ids);
    Promotions findByPromotionIdAndDeletedAtIsNull(Integer id);
    boolean existsByCodeAndDeletedAtIsNull(String code);

}
